package com.baman.manex.ui.earn.earnLocalStore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.controls.AdapterCallBack
import com.baman.manex.controls.CustomRecyclerView
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.controls.CustomViewScrollCallBack
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.data.dto.SortDto
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.databinding.FragmentEarnlocalstoreBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.ui.common.SortDialogFragment
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.Preferences
import com.baman.manex.util.toPersianNumber
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_earnlocalstore.*
import kotlinx.android.synthetic.main.list_feature.view.*
import kotlinx.android.synthetic.main.show_more_toolbar.view.*
import javax.inject.Inject


class EarnLocalStoreFragment : BaseFragment(),
    Injectable, CustomViewCallBack,
    AdapterCallBack,
    CustomViewScrollCallBack, InternetConnection {


    lateinit var adapter: EarnLocalStoreAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: FragmentEarnlocalstoreBinding

    lateinit var data: ArrayList<OnlineStoreInsideDto>
    lateinit var sortData: List<SortDto>
    private var userHasCard = false

    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: EarnLocalStoreViewModel

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    var currnetPage = 1
    var startPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_earnlocalstore, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
            ViewModelProvider(activity!!, viewModelFactory).get(EarnLocalStoreViewModel::class.java)

        userHasCard = Preferences.getUserHasCard(context!!)

        getSortData()

        adapter = EarnLocalStoreAdapter {
            findNavController().navigate(
                EarnLocalStoreFragmentDirections.showDetails(
                    it!!, null
                )
            )
        }

        getManexCount()

        setupCustomRecyclerView()

        skeletonScreen = Skeleton.bind(recyclerView)
            .adapter(adapter)
            .count(4)
            .load(R.layout.adapter_local_store_skeleton)
            .shimmer(true)
            .show()

        viewModel.setBackPress(false)

        setToolbar()

        setFeatureHandle()

        viewModel.requestDto.observe(this, Observer {
            binding.feature.setFilterSelected(!viewModel.isInitialFilter())
            binding.feature.setSortSelected(it.sortItem != null)
        })

        setFabButton()

        setBackPressHandle()

    }

    private fun setFeatureHandle() {
        binding.feature.search.setOnClickListener {
            findNavController().navigate(EarnLocalStoreFragmentDirections.search())
        }

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
            findNavController().navigate(EarnLocalStoreFragmentDirections.filter())
        }
    }

    private fun setToolbar() {
        binding.toolbar.image_toolbar_back.setOnClickListener { findNavController().popBackStack() }

        binding.toolbar.text_toolbar_title.text = getString(R.string.earn_local_store_title)
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

    private fun setFabButton() {
        recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0 && !binding.fab.isShown) {
                    binding.fab.visibility = View.VISIBLE
                    binding.fab.animate().translationY(0f).setDuration(600L).start()
                } else if (dy > 0 && binding.fab.isShown) {
                    binding.fab.animate().translationY(500f).setDuration(1000L).start()
                    binding.fab.visibility = View.GONE
                }
            }
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(EarnLocalStoreFragmentDirections.map())
        }
    }

    private fun getSortData() {
        viewModel.sortData.observe(this, Observer {
            it.handle(this, activity) { data, code ->
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearSort()
        viewModel.resetFilter()
    }

    private fun setupCustomRecyclerView() {

        customRecyclerView = binding.customRecyclerView
        recyclerView = customRecyclerView.getRecyclerView()!!

        customRecyclerView.setCustomViewCallBack(this)
        customRecyclerView.setCustomViewScrollCallBack(this)
        customRecyclerView.setStatus(ListStatus.UNDEFINE)
        adapter.setAdapterCallBack(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun getStores(page: Int) {

        if (page == 1) {
            viewModel.refresh()
        } else {
            customRecyclerView.setStatus(ListStatus.LOADING_BOTTOM)
            viewModel.setRequestDto(page, viewModel.selectedSortData)
        }

        viewModel.showMoreLocalStoreData.observe(this, Observer {
            it.handle(this, requireActivity(), skeletonScreen) { data, code ->

                fab.visibility = View.VISIBLE

                customRecyclerView.setSwipeRefreshStatus(true)
                customRecyclerView.setStatus(ListStatus.SUCCESS)
                adapter.setLocalStoreList(userHasCard, data.data[0].branches, currnetPage)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
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
        fab.visibility = View.GONE
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