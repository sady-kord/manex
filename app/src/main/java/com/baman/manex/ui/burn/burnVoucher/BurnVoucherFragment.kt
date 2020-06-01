package com.baman.manex.ui.burn.burnVoucher

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
import com.baman.manex.data.dto.SortDto
import com.baman.manex.data.dto.VoucherInsideDto
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.databinding.FragmentBurnVoucherBinding
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
import javax.inject.Inject

class BurnVoucherFragment : BaseFragment(),
    Injectable, CustomViewCallBack,
    AdapterCallBack,
    CustomViewScrollCallBack, InternetConnection {

    lateinit var adapter: BurnVoucherAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: FragmentBurnVoucherBinding

    lateinit var data: ArrayList<VoucherInsideDto>

    lateinit var sortData: List<SortDto>

    private lateinit var viewModel: BurnVoucherViewModel

    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    var currnetPage = 1
    var startPage = 1
    var loadedPage = 0
    var list = mutableListOf<VoucherInsideDto>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_burn_voucher, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(activity!!, viewModelFactory)
            .get(BurnVoucherViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = BurnVoucherAdapter(appExecutors, itemClickCallback = {
            findNavController().navigate(BurnVoucherFragmentDirections.showDetails(it!!))
        })



        setupCustomRecyclerView()

        getSortData()

        skeletonScreen = Skeleton.bind(recyclerView)
            .adapter(adapter)
            .count(4)
            .load(R.layout.adapter_voucher_code_item_skeleton)
            .shimmer(true)
            .show()

        getManexCount()

        viewModel.setBackPress(false)

        setToolbar()

        setFeatureHandle()

        viewModel.requestDto.observe(this, Observer {
            binding.feature.setFilterSelected(!viewModel.isInitialFilter())
            binding.feature.setSortSelected(it.sortItem != null)
        })

        setBackPressHandle()

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

    private fun setToolbar() {
        binding.toolbar.image_toolbar_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.text_toolbar_title.text = getString(R.string.burn_manex)
    }

    private fun setFeatureHandle() {

        binding.feature.search.setOnClickListener {
            findNavController().navigate(BurnVoucherFragmentDirections.search())

        }

        binding.feature.filter.setOnClickListener {
            findNavController().navigate(BurnVoucherFragmentDirections.filter())
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
    }

    private fun getSortData() {
        viewModel.sortData.observe(this@BurnVoucherFragment, Observer {
            it.handle(this, activity) { data, code ->
                this.sortData = data
                for (i in data.indices) {
                    if (data[i].checked) {

                        viewModel.defaultSortData = data[i].key

                    }
                }
                currnetPage = 1
                getVouchers(1)
            }
        })

    }

    private fun setupCustomRecyclerView() {

        customRecyclerView = binding.customRecyclerView
        recyclerView = customRecyclerView.getRecyclerView()!!

        customRecyclerView.setCustomViewCallBack(this)
        customRecyclerView.setCustomViewScrollCallBack(this)
        customRecyclerView.setStatus(ListStatus.UNDEFINE)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun getVouchers(page: Int) {
        if (page == 1) {
            viewModel.refresh()
        } else {
            customRecyclerView.setStatus(ListStatus.LOADING_BOTTOM)
            viewModel.setRequestDto(page, viewModel.selectedSortData)
        }

        viewModel.getResult.observe(this, Observer {
            it.handle(this, requireActivity(), skeletonScreen) { it, code ->

                customRecyclerView.setSwipeRefreshStatus(true)
                adapter.notifyDataSetChanged()
                customRecyclerView.setStatus(ListStatus.SUCCESS)

                if (!it.data[0].vouchers.isNullOrEmpty()) {
                    if (loadedPage != currnetPage || currnetPage == 1) {
                        loadedPage = currnetPage
                        if (loadedPage == 1) {
                            list = mutableListOf()
                            list.addAll(it.data[0].vouchers)
                            adapter.submitList(list)
                        } else {
                            list.addAll(it.data[0].vouchers)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearSort()
        viewModel.resetFilter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onScrollChange(scrollDirection: ScrollDirection?) {
    }

    override fun onRetryClicked() {
        getVouchers(startPage)
    }

    override fun onRefresh(page: Int) {
        viewModel.resetFilter()
        viewModel.clearSort()
        getSortData()
    }

    override fun showNoConnection() {
        adapter.notifyDataSetChanged()
        customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)
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
        getVouchers(currnetPage)
    }


}