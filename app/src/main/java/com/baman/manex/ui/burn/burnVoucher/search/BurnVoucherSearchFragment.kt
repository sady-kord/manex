package com.baman.manex.ui.burn.burnVoucher.search

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baman.manex.R
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.data.dto.BurnDto
import com.baman.manex.data.dto.VoucherResultDto
import com.baman.manex.di.Injectable
import com.baman.manex.di.ViewModelFactory
import com.baman.manex.network.Resource
import com.baman.manex.network.service.SearchTag
import com.baman.manex.ui.baseClass.BaseSearchFragment
import com.baman.manex.ui.burn.VoucherAdapter
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.autoCleared
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import javax.inject.Inject

class BurnVoucherSearchFragment : BaseSearchFragment(), Injectable, InternetConnection,
    CustomViewCallBack {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: BurnVoucherSearchViewModel

    private lateinit var searchTags: List<SearchTag>

    var adapter by autoCleared<VoucherAdapter>()

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen
    private lateinit var chipSkeletonScreen: ViewSkeletonScreen

    override fun setup() {

        adapter = VoucherAdapter(context!!) {
            findNavController().navigate(BurnVoucherSearchFragmentDirections.showDetails(it))
        }

        getSearchControl().queryHint = "نام فروشگاه مورد نظر شما"
        getTagsTitle().text = "دسته‌بندی فروشگاه‌ها"

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(BurnVoucherSearchViewModel::class.java)


        getList().adapter = adapter
        getList().layoutManager = LinearLayoutManager(context)

        setupRecyclerCallback(this)

        viewModel.query.value.let {
            if (it.isNullOrEmpty()) {
                viewModel.selectedTagKey.value.let {
                    if (it == null) {
                        getChipGroup().visibility = View.VISIBLE
                        getTagsTitle().visibility = View.VISIBLE
                        getChipDivider().visibility = View.VISIBLE
                    } else {
                        getChipGroup().visibility = View.GONE
                        getTagsTitle().visibility = View.GONE
                        getChipDivider().visibility = View.GONE
                    }
                }
            }
        }

        skeletonScreen = Skeleton.bind(getList())
            .adapter(adapter)
            .count(4)
            .load(R.layout.adapter_voucher_code_item_skeleton)
            .shimmer(true)
            .show()

        if (getChipGroup().visibility == View.VISIBLE) {
            chipSkeletonScreen = Skeleton.bind(getChipGroup())
                .load(R.layout.chips_skeleton)
                .shimmer(true)
                .show()
        }

        getSearchTags()
        getSearchResult()
        getTagResult()


    }

    override fun onClearClick() {
        viewModel.selectedTagKey.value.let {
            if(it != null){
                searchTags.forEachIndexed { index,res ->
                    if(res.key == viewModel.selectedTagKey.value!!.key){
                        setTags(searchTags,index)
                    }
                }
            }
        }
    }

    private fun getSearchTags() {

        viewModel.searchTags.observe(this, Observer { res ->

            if (::chipSkeletonScreen.isInitialized) {
                res.handle(this, requireActivity(), null, chipSkeletonScreen) {it,code->
                    searchTags = it
                    setTags(searchTags,0)
                    viewModel.selectedTagKey.value.let {
                        if(it != null){
                            searchTags.forEachIndexed { index,res ->
                                if(res.key == viewModel.selectedTagKey.value!!.key){
                                    setTags(searchTags,index)
                                }
                            }
                        }
                    }
                }
            } else {
                res.handle(this, requireActivity()) {it,code->
                    searchTags = it
                    setTags(searchTags,0)
                    viewModel.selectedTagKey.value.let {
                        if(it != null){
                            searchTags.forEachIndexed { index,res ->
                                if(res.key == viewModel.selectedTagKey.value!!.key){
                                    setTags(searchTags,index)
                                }
                            }
                        }
                    }
                }
            }

        })
    }

    private fun getSearchResult() {
        val observer = Observer<Resource<VoucherResultDto>> { res ->
            res.handle(this, requireActivity(), skeletonScreen) {it,code->
                setLoadingOnSearch(false)
                if (it.data[0].vouchers.isEmpty()) {
                    setUpRecyclerNoDataFound()
                } else {
                    setupRecyclerSuccess(false)
                    adapter.setData(it.data[0].vouchers)
                }
            }
        }
        viewModel.querySelectionResult.observe(this, observer)
    }

    private fun getTagResult() {
        viewModel.tagSelectionResult.observe(this, Observer {
            it.handle(this, requireActivity(), skeletonScreen) {it,code->
                if (it.data[0].vouchers.isEmpty()) {
                    setUpRecyclerNoDataFound()
                } else {
                    setupRecyclerSuccess(true)
                    adapter.setData(it.data[0].vouchers)
                }
            }
        })
    }

    override fun onTagSelected(key: String) {
        searchTags.forEach {
            if (it.title == key)
                viewModel.onTagSelected(it)
        }
    }

    override fun onQueryChanged(query: String) {
        viewModel.setQuery(query)
    }

    override fun showNoConnection() {
        adapter.notifyDataSetChanged()
        setupRecyclerFailed()

        getTagsTitle().visibility = View.GONE
    }

    override fun onRetryClicked() {
        getSearchTags()
        getSearchResult()
    }

    override fun onRefresh(page: Int) {
        getSearchTags()
        getSearchResult()
    }

}