package com.baman.manex.ui.profile.waitingManex

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
import com.baman.manex.controls.CustomRecyclerView
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.controls.CustomViewScrollCallBack
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.databinding.FragmentWaitingManexBinding
import com.baman.manex.di.Injectable
import com.baman.manex.network.service.FilterTransactionRequestDto
import com.baman.manex.network.service.GetTransactionRequestDto
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.ui.profile.myTransaction.MyTransactionAdapter
import com.baman.manex.ui.profile.myTransaction.MyTransactionViewModel
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.Preferences
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.layout_no_data_found.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import javax.inject.Inject

class WaitingManexFragment : BaseFragment(), Injectable, CustomViewCallBack,
    CustomViewScrollCallBack,
    InternetConnection {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var binding: FragmentWaitingManexBinding
    private lateinit var viewModel: MyTransactionViewModel

    private lateinit var adapter : MyTransactionAdapter
    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_waiting_manex, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
            ViewModelProvider(activity!!, viewModelFactory).get(MyTransactionViewModel::class.java)

        adapter = MyTransactionAdapter()

        setupCustomRecyclerView()

        skeletonScreen = Skeleton.bind(recyclerView)
            .adapter(adapter)
            .load(R.layout.adapter_my_transaction_skeleton)
            .count(4)
            .shimmer(true)
            .show()

        getTransaction()

        binding.toolbar.setFongroundTintResource(R.color.black)

        binding.toolbar.image_up.setOnClickListener { findNavController().popBackStack() }

        binding.toolbar.text_title.text = getString(R.string.waiting_manex_title)

        Preferences.getAccessToken(requireContext())

    }

    private fun getTransaction(){
        viewModel.refresh()

        var types = mutableListOf<Int>()
        types.add(2)

        var request = GetTransactionRequestDto(null,
            FilterTransactionRequestDto(types,null,null, mutableListOf()))

        viewModel.setRequestDtoForWaitingManex(request)

        viewModel.getTransactions.observe(this, Observer { res ->
            res.handle(this,activity,skeletonScreen,null,
                null,null,null,{msg,code ->
                    customRecyclerView.visibility = View.GONE
                    binding.noDataFoundLayout.visibility = View.VISIBLE

                    binding.noDataFoundLayout.image_view.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_no_search_found
                        )
                    )
                    binding.noDataFoundLayout.title.text =
                        getString(R.string.no_transaction_found_filtered)
                    binding.noDataFoundLayout.sub_title.text =
                        getString(R.string.no_transaction_found_desc_filtered)
            }) {it,code->

                if (it.isNotEmpty()){
                    customRecyclerView.setSwipeRefreshStatus(true)
                    adapter.notifyDataSetChanged()
                    customRecyclerView.setStatus(ListStatus.SUCCESS)

                    adapter.setData(it)
                    customRecyclerView.visibility = View.VISIBLE
                    binding.noDataFoundLayout.visibility = View.GONE
                }else {
                    customRecyclerView.visibility = View.GONE
                    binding.noDataFoundLayout.visibility = View.VISIBLE

                    binding.noDataFoundLayout.image_view.
                        setImageDrawable(resources.getDrawable(R.drawable.ic_no_search_found))
                    binding.noDataFoundLayout.title.text = getString(R.string.no_transaction_found_filtered)
                    binding.noDataFoundLayout.sub_title.text = getString(R.string.no_transaction_found_desc_filtered)
                }

            }
        })
    }

    private fun setupCustomRecyclerView(){

        customRecyclerView = binding.customRecyclerView

        recyclerView = customRecyclerView.getRecyclerView()!!

        customRecyclerView.setCustomViewCallBack(this)

        customRecyclerView.setCustomViewScrollCallBack(this)

        recyclerView?.adapter = adapter

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
        getTransaction()
    }

    override fun onRefresh(page: Int) {
        getTransaction()
    }

    override fun showNoConnection() {
        adapter.notifyDataSetChanged()
        customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)
    }
}