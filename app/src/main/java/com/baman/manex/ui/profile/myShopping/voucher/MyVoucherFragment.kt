package com.baman.manex.ui.profile.myShopping.voucher

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.databinding.FragmentMyVoucherBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.ui.profile.myShopping.MyShoppingFragmentDirections
import com.baman.manex.ui.profile.myShopping.MyShoppingViewModel
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.autoCleared
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.layout_no_data_found.view.*
import javax.inject.Inject

class MyVoucherFragment : BaseFragment(), Injectable, CustomViewCallBack,
    CustomViewScrollCallBack,
    AdapterCallBack,
    InternetConnection {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentMyVoucherBinding>()

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: MyShoppingViewModel

    private lateinit var myVoucherAdapter: MyVoucherAdapter
    var currnetPage = 1
    var startPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_voucher, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(MyShoppingViewModel::class.java)

        getVouchers(startPage)
        myVoucherAdapter = MyVoucherAdapter()

        myVoucherAdapter.setItemSelectedCallBack(object :
            MyVoucherAdapter.MyVoucherAdapterCallBack {
            override fun itemSelectedCallBack(id: Long) {
                findNavController().navigate(
                    MyShoppingFragmentDirections.purchase(id.toString())
                )
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setupCustomRecyclerView()
        if (myVoucherAdapter.list.isEmpty())
            skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(myVoucherAdapter)
                .load(R.layout.adapter_my_voucher_skeleton)
                .count(4)
                .shimmer(true)
                .show()


        //getVouchers(startPage)

    }

    private fun getVouchers(page: Int) {


        if (page == 1) {
            viewModel.setVoucherRequest(1)
        } else {
            customRecyclerView.setStatus(ListStatus.LOADING_BOTTOM)
            viewModel.setVoucherRequest(page)
        }

        viewModel.voucherData.observe(this, Observer {
            it.handle(this, requireActivity(), skeletonScreen) { it, code ->

                if (it.data[0].list.isNotEmpty()) {

                    customRecyclerView.setSwipeRefreshStatus(true)
                    customRecyclerView.setStatus(ListStatus.SUCCESS)
                    binding.customRecyclerView.visibility = View.VISIBLE
                    binding.noDataFoundLayout.visibility = View.GONE
                    myVoucherAdapter.setData(it.data[0].list, currnetPage)

                } else {

                    if (currnetPage == 1) {
                        binding.customRecyclerView.visibility = View.GONE
                        binding.noDataFoundLayout.visibility = View.VISIBLE

                        binding.noDataFoundLayout.image_view.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ic_no_voucher
                            )
                        )
                        binding.noDataFoundLayout.title.text = getString(R.string.no_voucher_found)
                        binding.noDataFoundLayout.sub_title.text =
                            getString(R.string.no_voucher_found_desc)
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

        myVoucherAdapter.setAdapterCallBack(this)

        recyclerView.adapter = myVoucherAdapter

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

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onRetryClicked() {
        currnetPage = 1
        getVouchers(startPage)
    }

    override fun onRefresh(page: Int) {
        currnetPage = 1
        getVouchers(startPage)
    }

    override fun showNoConnection() {
        myVoucherAdapter.notifyDataSetChanged()
        customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)

    }

    override fun richToEnd() {
        currnetPage += 1
        getVouchers(currnetPage)
    }

}