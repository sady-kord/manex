package com.baman.manex.ui.home.search

import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baman.manex.AppExecutors
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.data.dto.*
import com.baman.manex.data.model.ListStatus
import com.baman.manex.di.Injectable
import com.baman.manex.di.ViewModelFactory
import com.baman.manex.network.Resource
import com.baman.manex.ui.baseClass.BaseSearchFragment
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.KeyBoardHelper
import com.baman.manex.util.autoCleared
import javax.inject.Inject

class HomeSearchFragment : BaseSearchFragment(), Injectable, InternetConnection,
    CustomViewCallBack {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var viewModel: HomeSearchViewModel

    var adapter by autoCleared<HomeSearchAdapter>()

    override fun setup() {

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeSearchViewModel::class.java)

        adapter =
            HomeSearchAdapter(appExecutors) { it, isEarn, isOnline ->
                when (it) {
                    is ManexStoreInsideDto -> {
                        findNavController().navigate(
                            HomeSearchFragmentDirections.showDetailsStoreManex(
                                it
                            )
                        )
                    }
                    is StoreInfoDto -> {
                        if (isEarn!!) {
                            if (isOnline!!) {
                                findNavController().navigate(
                                    HomeSearchFragmentDirections.showDetailsOnlineStore(
                                        it,
                                        true
                                    )
                                )
                            } else {
                                findNavController().navigate(
                                    HomeSearchFragmentDirections.showDetailsLocalStore(
                                        it,"search"
                                    )
                                )
                            }
                        } else {
                            findNavController().navigate(
                                HomeSearchFragmentDirections.showDetailsOnlineStore(
                                    it,
                                    false
                                )
                            )
                        }
                    }
                    is VoucherInsideDto -> {
                        findNavController().navigate(
                            HomeSearchFragmentDirections.showDetailsVoucher(
                                it
                            )
                        )
                    }
                }
            }

        getChipGroup().visibility = View.GONE
        getTagsTitle().visibility = View.GONE
        getChipDivider().visibility = View.GONE

        getList().adapter = adapter
        getList().layoutManager = LinearLayoutManager(context)

        setFocusOnSearchView()

        setupRecyclerCallback(this)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            getSearchResult()
        }, 300)
        
    }

    private fun getSearchResult() {
        viewModel.refresh()
        val observer = Observer<Resource<PartnerSearchDto>> { res ->
            res.handle(this, requireActivity()) {it,code->
                setLoadingOnSearch(false)
                if (it.burn!!.vocher.vochers.isNullOrEmpty() &&
                    it.burn!!.onLine.onlines.isNullOrEmpty() &&
                    it.burn!!.manexStore!!.stores.isNullOrEmpty() &&
                    it.earn!!.onLine.branches.isNullOrEmpty() &&
                    it.earn!!.offline.branches.isNullOrEmpty()
                ) {
                    getCustomRecycler().setStatus(ListStatus.SEARCH_EMPTY)
                } else {
                    setupRecyclerSuccess(false)
                    var list = mutableListOf<Any>()

                    if (!it.earn!!.onLine.branches.isNullOrEmpty() ||
                        !it.earn!!.offline.branches.isNullOrEmpty()
                    ) {
                        list.add(it.earn!!)
                    }

                    if (!it.burn!!.manexStore!!.stores.isNullOrEmpty() ||
                        !it.burn!!.onLine.onlines.isNullOrEmpty() ||
                        !it.burn!!.vocher.vochers.isNullOrEmpty()
                    ) {
                        list.add(it.burn!!)
                    }

                    adapter.submitList(list)
                }
            }
        }
        viewModel.querySelectionResult.observe(this, observer)
    }

    override fun onQueryChanged(query: String) {
        setLoadingOnSearch(true)
        viewModel.setQuery(query)
    }

    override fun showNoConnection() {
        adapter.notifyDataSetChanged()
        setupRecyclerFailed()
    }

    override fun onRetryClicked() {
        getSearchResult()
    }

    override fun onRefresh(page: Int) {
        getSearchResult()
    }

}