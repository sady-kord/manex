package com.baman.manex.ui.profile.myShopping.manexStore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.controls.AdapterCallBack
import com.baman.manex.controls.CustomRecyclerView
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.controls.CustomViewScrollCallBack
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.databinding.FragmentMyManexStoreBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.ui.profile.myShopping.MyShoppingViewModel
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.autoCleared
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.layout_no_data_found.view.*
import javax.inject.Inject

class MyManexStoreFragment : BaseFragment(), Injectable, CustomViewCallBack,
    CustomViewScrollCallBack,
    AdapterCallBack,
    InternetConnection {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentMyManexStoreBinding>()

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen


    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: MyShoppingViewModel

    private lateinit var myManexStoreAdapter: MyManexStoreAdapter

    var currnetPage = 1
    var startPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_manex_store, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(MyShoppingViewModel::class.java)

        myManexStoreAdapter = MyManexStoreAdapter()

        setupCustomRecyclerView()

        skeletonScreen = Skeleton.bind(recyclerView)
            .adapter(myManexStoreAdapter)
            .load(R.layout.adapter_my_manex_store_skeleton)
            .count(4)
            .shimmer(true)
            .show()


        getShops(startPage)

    }

    fun getShops(page: Int) {
        if (page == 1) {
            viewModel.setShopRequest(1)
        } else {
            customRecyclerView.setStatus(ListStatus.LOADING_BOTTOM)
            viewModel.setShopRequest(page)
        }
        viewModel.shopData.observe(this, Observer {
            it.handle(this, requireActivity(), skeletonScreen) { it, code ->

                if (it.data[0].list.isNotEmpty()) {

                    customRecyclerView.setSwipeRefreshStatus(true)
                    customRecyclerView.setStatus(ListStatus.SUCCESS)
                    binding.customRecyclerView.visibility = View.VISIBLE
                    binding.noDataFoundLayout.visibility = View.GONE
                    myManexStoreAdapter.setData(it.data[0].list, currnetPage)
                } else {
                    if (currnetPage == 1) {
                        binding.customRecyclerView.visibility = View.GONE
                        binding.noDataFoundLayout.visibility = View.VISIBLE

                        binding.noDataFoundLayout.image_view.setImageDrawable(
                            VectorDrawableCompat.create(
                                resources,
                                R.drawable.ic_no_my_manex,
                                null
                            )
                        )
                        binding.noDataFoundLayout.title.text =
                            getString(R.string.no_item_manex_found)
                        binding.noDataFoundLayout.sub_title.text =
                            getString(R.string.no_item_manex_found_desc)
                    }
                }

            }
        })
    }

    private fun setupCustomRecyclerView() {

        customRecyclerView = binding.customRecyclerView

        recyclerView = customRecyclerView.getRecyclerView()!!

        customRecyclerView.setCustomViewCallBack(this)

        customRecyclerView.setCustomViewScrollCallBack(this)

        myManexStoreAdapter.setAdapterCallBack(this)

        recyclerView.adapter = myManexStoreAdapter

        recyclerView.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )

        customRecyclerView.setStatus(ListStatus.UNDEFINE)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }


    override fun onScrollChange(scrollDirection: ScrollDirection?) {
    }

    override fun onRetryClicked() {
        currnetPage = 1
        getShops(startPage)
    }

    override fun onRefresh(page: Int) {
        currnetPage = 1
        getShops(startPage)
    }

    override fun showNoConnection() {
        myManexStoreAdapter.notifyDataSetChanged()
        customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)

    }

    override fun richToEnd() {
        currnetPage += 1
        getShops(currnetPage)
    }

}