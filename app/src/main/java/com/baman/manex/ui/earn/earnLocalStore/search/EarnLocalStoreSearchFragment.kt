package com.baman.manex.ui.earn.earnLocalStore.search

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baman.manex.R
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.data.dto.BranchResultDto
import com.baman.manex.data.model.ListResultType
import com.baman.manex.di.Injectable
import com.baman.manex.di.ViewModelFactory
import com.baman.manex.network.Resource
import com.baman.manex.network.service.SearchTag
import com.baman.manex.ui.baseClass.BaseSearchFragment
import com.baman.manex.ui.baseClass.SearchAdapter
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.autoCleared
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import javax.inject.Inject

class EarnLocalStoreSearchFragment : BaseSearchFragment(), Injectable, InternetConnection,
    CustomViewCallBack {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: EarnLocalStoreSearchViewModel

    var adapter by autoCleared<SearchAdapter>()

    private lateinit var searchTags: List<SearchTag>
    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen
    private lateinit var chipSkeletonScreen: ViewSkeletonScreen

    override fun setup() {

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(EarnLocalStoreSearchViewModel::class.java)


        adapter = SearchAdapter({
            findNavController().navigate(
                EarnLocalStoreSearchFragmentDirections.showDetails(
                    it,"search"
                )
            )
        }, { type, page ->

            when (type) {
                ListResultType.SearchByName -> {
                    viewModel.setMoreRequest(page, name = true, address = false)
                }
                ListResultType.SearchByAddress -> {
                    viewModel.setMoreRequest(page, name = false, address = true)
                }
            }

        })

        getSearchControl().queryHint = "نام فروشگاه و یا محل مورد نظر شما"
        getTagsTitle().text = "دسته‌بندی فروشگاه‌ها"

        getList().adapter = adapter
        getList().layoutManager = LinearLayoutManager(context)

        setupRecyclerCallback(this)

        skeletonScreen = Skeleton.bind(getList())
            .adapter(adapter)
            .count(4)
            .load(R.layout.adapter_local_store_skeleton)
            .shimmer(true)
            .show()

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

        if (getChipGroup().visibility == View.VISIBLE) {
            chipSkeletonScreen = Skeleton.bind(getChipGroup())
                .load(R.layout.chips_skeleton)
                .shimmer(true)
                .show()
        }

        getSearchTags()
        getSearchResult()
        getMoreSearchResult()
        getSearchTagResult()
    }

    private fun getSearchTags() {

        viewModel.searchTags.observe(this, Observer { res ->

            if (::chipSkeletonScreen.isInitialized) {
                res.handle(this, requireActivity(), null, chipSkeletonScreen) {it,code->
                    searchTags = it
                    setTags(searchTags, 0)
                    viewModel.selectedTagKey.value.let {
                        if (it != null) {
                            searchTags.forEachIndexed { index, res ->
                                if (res.key == viewModel.selectedTagKey.value!!.key) {
                                    setTags(searchTags, index)
                                }
                            }
                        }
                    }
                }
            } else {
                res.handle(this, requireActivity()) {it,code->
                    searchTags = it
                    setTags(searchTags, 0)
                    viewModel.selectedTagKey.value.let {
                        if (it != null) {
                            searchTags.forEachIndexed { index, res ->
                                if (res.key == viewModel.selectedTagKey.value!!.key) {
                                    setTags(searchTags, index)
                                }
                            }
                        }
                    }
                }
            }

        })
    }

    private fun getSearchResult() {
        val observer = Observer<Resource<BranchResultDto>> { res ->
            res.handle(this, requireActivity(), skeletonScreen) {it,code->
                setLoadingOnSearch(false)

                if (it.totalCount == 0) {
                    setUpRecyclerNoDataFound()
                } else {
                    setupRecyclerSuccess(false)
                    adapter.setData(it.data)
                }

            }
        }
        viewModel.querySelectionResult.observe(this, observer)
    }

    private fun getMoreSearchResult() {
        val observer = Observer<Resource<BranchResultDto>> { res ->
            res.handle(this, requireActivity()) {it,code->
                setLoadingOnSearch(false)

                if (it.totalCount == 0) {
                    setUpRecyclerNoDataFound()
                } else {
                    setupRecyclerSuccess(false)
                    adapter.setData(it.data)
                }

            }
        }
        viewModel.moreSelectionResult.observe(this, observer)
    }

    private fun getSearchTagResult() {
        viewModel.tagSelectionResult.observe(this, Observer {
            it.handle(this, requireActivity(), skeletonScreen) {it,code->
                if (it.totalCount == 0) {
                    setUpRecyclerNoDataFound()
                } else {
                    setupRecyclerSuccess(true)
                    adapter.setData(it.data)
                }
            }
        })
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