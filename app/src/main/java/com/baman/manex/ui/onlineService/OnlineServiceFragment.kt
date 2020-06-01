package com.baman.manex.ui.onlineService

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.controls.CustomRecyclerView
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.controls.CustomViewScrollCallBack
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.databinding.FragmentEarnOnlineBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.toPersianNumber
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.show_more_toolbar.view.*
import javax.inject.Inject

class OnlineServiceFragment : BaseFragment(), Injectable, CustomViewCallBack,
    CustomViewScrollCallBack, InternetConnection {

    lateinit var adapter: OnlineServiceAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var binding: FragmentEarnOnlineBinding
    private lateinit var viewModel: OnlineServiceViewModel

    val args: OnlineServiceFragmentArgs by navArgs()

    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView
    private var isEarn = false

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(layoutInflater, R.layout.fragment_earn_online, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(OnlineServiceViewModel::class.java)

        isEarn = args.isEarn

        adapter = OnlineServiceAdapter(appExecutors, isEarn, itemClickCallback = {
            findNavController().navigate(
                OnlineServiceFragmentDirections.showOnlineStoreDetail(
                    it,
                    isEarn
                )
            )
        })

        getManexCount()

        setupCustomRecyclerView()

        skeletonScreen = Skeleton.bind(recyclerView)
            .adapter(adapter)
            .count(4)
            .load(R.layout.adapter_online_item_skeleton)
            .shimmer(true)
            .show()

        getData()

        binding.toolbar.image_toolbar_back.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setupCustomRecyclerView() {

        customRecyclerView = binding.customRecyclerView
        recyclerView = customRecyclerView.getRecyclerView()!!

        customRecyclerView.setCustomViewCallBack(this)
        customRecyclerView.setCustomViewScrollCallBack(this)
        customRecyclerView.setStatus(ListStatus.UNDEFINE)

        recyclerView?.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
    }

    private fun getData(){
        if (isEarn) {
            binding.toolbar.text_toolbar_title.text = getString(R.string.earn_online_activity_title)
            getEarnList()

        } else {
            binding.toolbar.text_toolbar_title.text = getString(R.string.burn_manex)
            getBurnList()

        }
    }

    private fun getEarnList() {
        viewModel.refresh()
        viewModel.showMoreOnlineData.observe(this, Observer {
            it.handle(this, activity,skeletonScreen) { data,code->

                customRecyclerView.setSwipeRefreshStatus(true)
                adapter.notifyDataSetChanged()
                customRecyclerView.setStatus(ListStatus.SUCCESS)
//                adapter.submitList(data.onLine.branches)
            }
        })
    }

    private fun getBurnList() {
        viewModel.refresh()
        viewModel.showMoreOnlineBurnData.observe(this, Observer {
            it.handle(this, activity) { it,code->
                skeletonScreen.hide()
                customRecyclerView.setSwipeRefreshStatus(true)
                adapter.notifyDataSetChanged()
                customRecyclerView.setStatus(ListStatus.SUCCESS)
//                adapter.submitList(data.onLine!!.onlines)
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
        getData()
    }

    override fun onRefresh(page: Int) {
        getData()
    }

    override fun showNoConnection() {
        adapter.notifyDataSetChanged()
        customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)
    }

    private fun getManexCount(){
        viewModel.walletData.observe(this, Observer { res ->
            res.handle(this, requireActivity()) {it,code->
                binding.toolbar.text_toolbar_manexcount.text = it.manexAmount.toInt().toString().toPersianNumber()
            }
        })
    }
}
