package com.baman.manex.ui.profile.myTransaction.search

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baman.manex.R
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.data.dto.MyTransactionGroupDto
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseSearchFragment
import com.baman.manex.ui.profile.myTransaction.MyTransactionAdapter
import com.baman.manex.util.InternetConnection
import com.baman.manex.network.Resource
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import javax.inject.Inject

class MyTransactionSearchFragment : BaseSearchFragment(), Injectable, InternetConnection,
    CustomViewCallBack {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MyTransactionSearchViewModel
    private lateinit var adapter: MyTransactionAdapter
    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    override var hasTags = false

    override fun setup() {
        getChipGroup().visibility = View.GONE
        getTagsTitle().visibility = View.GONE
        getChipDivider().visibility = View.GONE

//        val hPadding =
//            resources.getDimensionPixelSize(R.dimen.transactionsearch_list_horizontalpadding)
//        getList().setPadding(hPadding, getList().paddingTop, hPadding, getList().paddingBottom)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MyTransactionSearchViewModel::class.java)

        adapter = MyTransactionAdapter()

        setupRecyclerCallback(this)

        getSearchControl().queryHint =
            "تراکنش مورد نظر شما" // todo: fix this based on server search criteria

        getList().adapter = adapter

        getList().layoutManager = LinearLayoutManager(context)


        skeletonScreen = Skeleton.bind(getList())
            .adapter(adapter)
            .count(4)
            .load(R.layout.adapter_my_transaction_skeleton)
            .shimmer(true)
            .show()

        getSearchData()

    }

    private fun getSearchData() {

        var observer = Observer<Resource<List<MyTransactionGroupDto>>> { res ->
            res.handle(this, requireActivity(),skeletonScreen) {it,code->
                setLoadingOnSearch(false)
                if (it.isEmpty()) {
                    setUpRecyclerNoDataFound(true)
                } else {
                    setupRecyclerSuccess(false)
                    adapter.setData(it)
                }

            }
        }

        viewModel.querySelectionResult.observe(this,observer)
    }

    override fun onQueryChanged(query: String) {
        setLoadingOnSearch(true)
        viewModel.setQuery(query)
    }

    override fun showNoConnection() {
        adapter.notifyDataSetChanged()
        setupRecyclerFailed()
        getTagsTitle().visibility = View.GONE
    }

    override fun onRetryClicked() {
        getSearchData()
    }

    override fun onRefresh(page: Int) {
        getSearchData()
    }

}