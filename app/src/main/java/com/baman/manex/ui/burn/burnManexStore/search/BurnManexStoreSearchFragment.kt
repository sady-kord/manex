package com.baman.manex.ui.burn.burnManexStore.search

import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.data.dto.BurnDto
import com.baman.manex.data.dto.ShopResultDto
import com.baman.manex.di.Injectable
import com.baman.manex.di.ViewModelFactory
import com.baman.manex.network.service.SearchTag
import com.baman.manex.ui.baseClass.BaseSearchFragment
import com.baman.manex.ui.burn.burnManexStore.BurnManexStoreAdapter
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.autoCleared
import com.baman.manex.network.Resource
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import kotlinx.android.synthetic.main.list_feature.*
import kotlinx.android.synthetic.main.view_pager.view.*
import javax.inject.Inject

class BurnManexStoreSearchFragment : BaseSearchFragment(), Injectable
    , InternetConnection,
    CustomViewCallBack {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var appExceptors: AppExecutors

    private lateinit var searchTags: List<SearchTag>

    private lateinit var viewModel: BurnManexStoreSearchViewModel
    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen
    private lateinit var chipSkeletonScreen: ViewSkeletonScreen

    var adapter by autoCleared<BurnManexStoreSearchAdapter>()

    override fun setup() {

        adapter =
            BurnManexStoreSearchAdapter(appExceptors) {
                findNavController().navigate(BurnManexStoreSearchFragmentDirections.showDetails(it!!))
            }

        getSearchControl().queryHint = "نام کالای مورد نظر شما"
        getTagsTitle().text = "دسته‌بندی کالاها"

        viewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(BurnManexStoreSearchViewModel::class.java)

        getList().adapter = adapter
        getList().layoutManager = LinearLayoutManager(requireContext())


        setupRecyclerCallback(this)

        skeletonScreen = Skeleton.bind(getList())
            .adapter(adapter)
            .count(4)
            .load(R.layout.adapter_manex_store_search_skeleton)
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

        viewModel.selectedTagKey.let {
            viewModel.getLastTagData()
        }

        if (getChipGroup().visibility == View.VISIBLE) {
            chipSkeletonScreen = Skeleton.bind(getChipGroup())
                .load(R.layout.chips_skeleton)
                .shimmer(true)
                .show()
        }


        getSearchTags()
        getSearchResult()
        getSearchTagResult()

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

        viewModel.querySelectionResult.observe(this, Observer { res ->

            res.handle(this, requireActivity(), skeletonScreen) {it,code->
                setLoadingOnSearch(false)

                if (it.totalCount == 0) {
                    setUpRecyclerNoDataFound()
                } else {
                    setupRecyclerSuccess(false)
                    adapter.submitList(it.data[0].shops)
                }
            }

        })
    }

    private fun getSearchTagResult() {
        viewModel.tagSelectionResult.observe(this, Observer { res ->

            res.handle(this, requireActivity(), skeletonScreen) {it,code->

                if (it.totalCount == 0) {
                    setUpRecyclerNoDataFound()
                } else {
                    setupRecyclerSuccess(true)
                    adapter.submitList(it.data[0].shops)
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
        getSearchTags()
    }

    override fun onRefresh(page: Int) {
        getSearchTags()
        getSearchResult()
        getSearchTags()
    }
}