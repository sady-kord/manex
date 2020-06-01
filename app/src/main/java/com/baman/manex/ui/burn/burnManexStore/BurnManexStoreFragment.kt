package com.baman.manex.ui.burn.burnManexStore

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.controls.AdapterCallBack
import com.baman.manex.controls.CustomRecyclerView
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.controls.CustomViewScrollCallBack
import com.baman.manex.data.dto.ManexStoreInsideDto
import com.baman.manex.data.dto.SortDto
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.databinding.FragmentBurnStoreManexBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.ui.common.SortDialogFragment
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.toPersianNumber
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.list_feature.view.*
import kotlinx.android.synthetic.main.show_more_toolbar.view.*
import java.util.*
import javax.inject.Inject

class BurnManexStoreFragment : BaseFragment(),
    Injectable, CustomViewCallBack,
    AdapterCallBack,
    CustomViewScrollCallBack, InternetConnection {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: FragmentBurnStoreManexBinding

    lateinit var data: ArrayList<ManexStoreInsideDto>
    lateinit var sortData: List<SortDto>

    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen
    private lateinit var viewModel: BurnManexStoreViewModel
    private lateinit var adapter: BurnManexStoreAdapter

    var currnetPage = 1
    var startPage = 1
    var loadedPage = 0
    var list = mutableListOf<ManexStoreInsideDto>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_burn_store_manex, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
            ViewModelProvider(activity!!, viewModelFactory).get(BurnManexStoreViewModel::class.java)

        adapter = BurnManexStoreAdapter(
            appExecutors,
            itemClickCallback = {
                findNavController().navigate(BurnManexStoreFragmentDirections.showDetails(it!!))
            })

        setupCustomRecyclerView()

        skeletonScreen = Skeleton.bind(recyclerView)
            .adapter(adapter)
            .count(4)
            .load(R.layout.adapter_manex_store_item_skeleton)
            .shimmer(true)
            .show()

        getSortData()

        getManexCount()

        setFeatureHandle()

        viewModel.setBackPress(false)

        viewModel.requestDto.observe(this, Observer {
            binding.feature.setFilterSelected(!viewModel.isInitialFilter())
            binding.feature.setSortSelected(it.sortItem != null)
        })

        setToolbar()

        setBackPressHandle()


    }

    private fun setupCustomRecyclerView() {
        customRecyclerView = binding.customRecyclerView

        recyclerView = customRecyclerView.getRecyclerView()!!

        customRecyclerView.setCustomViewCallBack(this)
        customRecyclerView.setCustomViewScrollCallBack(this)
        customRecyclerView.setStatus(ListStatus.UNDEFINE)

        val layoutManager =  StaggeredGridLayoutManager (2 ,  StaggeredGridLayoutManager .VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.layoutDirection = View.LAYOUT_DIRECTION_RTL

        recyclerView.adapter = adapter

    }

    private fun setToolbar() {
        binding.toolbar.image_toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbar.text_toolbar_title.text = getString(R.string.burn_manex)
    }

    private fun setBackPressHandle() {
        val value = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setBackPress(true)

                findNavController().navigateUp()
                isEnabled = false
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, value)

    }

    private fun setFeatureHandle() {
        binding.feature.sort.setOnClickListener {
            if (::sortData.isInitialized) {
                var s: Int

                if (viewModel.selectedSortData == 0)
                    s = viewModel.defaultSortData
                else
                    s = viewModel.selectedSortData

                val sortDialogFragment =
                    SortDialogFragment.newInstance(sortData, s)

                sortDialogFragment.setCallBack(object : SortDialogFragment.DialogCallBack {
                    override fun onCallBack(key: Int) {
                        binding.feature.setSortSelected(true)

                        for (i in sortData.indices) {
                            if (sortData[i].key == key) {
                                viewModel.selectedSortData = sortData[i].key
                            }
                        }
                        viewModel.setSortKey(key)
                        currnetPage = 1

                    }
                })

                sortDialogFragment.show(childFragmentManager, "")
            }
        }

        binding.feature.filter.setOnClickListener {
            findNavController().navigate(BurnManexStoreFragmentDirections.filter())
        }

        binding.feature.search.setOnClickListener {
            findNavController().navigate(BurnManexStoreFragmentDirections.search())
        }
    }

    private fun getSortData() {
        viewModel.sortData.observe(this, Observer {
            it.handle(this, requireActivity()) { data, code ->
                this.sortData = data
                for (i in data.indices) {
                    if (data[i].checked) {
                        viewModel.defaultSortData = data[i].key
                    }
                }
                currnetPage = 1
                getStores(1)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun getStores(page: Int) {

        if (page == 1) {
            viewModel.refresh()
        } else {
            customRecyclerView.setStatus(ListStatus.LOADING_BOTTOM)
            viewModel.setRequestDto(page, viewModel.selectedSortData)
        }
        viewModel.showMoreManexStoreData.observe(this, Observer { res ->
            res.handle(this, activity) { it, code ->

                binding.feature.visibility = View.VISIBLE
                skeletonScreen.hide()

                customRecyclerView.setSwipeRefreshStatus(true)
                adapter.notifyDataSetChanged()
                customRecyclerView.setStatus(ListStatus.SUCCESS)

                if (!it.data[0].shops.isNullOrEmpty()) {
                    if (loadedPage != currnetPage || currnetPage == 1) {
                        loadedPage = currnetPage
                        if (loadedPage == 1) {
                            list = mutableListOf()
                            list.addAll(it.data[0].shops)
                            adapter.submitList(list)
                        } else {
                            list.addAll(it.data[0].shops)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }


                adapter.submitList(it.data[0].shops)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearSort()
        viewModel.resetFilter()
    }

    override fun onScrollChange(scrollDirection: ScrollDirection?) {
    }

    override fun onRetryClicked() {
        getStores(startPage)
    }

    override fun onRefresh(page: Int) {
        viewModel.clearSort()
        viewModel.resetFilter()
        getSortData()
    }

    override fun showNoConnection() {
        adapter.notifyDataSetChanged()
        customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)
        binding.feature.visibility = View.GONE
    }

    private fun getManexCount() {
        viewModel.walletData.observe(this, Observer { res ->
            res.handle(this, requireActivity()) { it, code ->
                binding.toolbar.text_toolbar_manexcount.text =
                    it.manexAmount.toInt().toString().toPersianNumber()
            }
        })
    }

    override fun richToEnd() {
        currnetPage += 1
        getStores(currnetPage)
    }
}