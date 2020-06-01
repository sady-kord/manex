package com.baman.manex.ui.profile.myCards
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.baman.manex.databinding.FragmentMyCardsBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.Preferences
import com.baman.manex.util.autoCleared
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.layout_no_data_found.view.*
import javax.inject.Inject
class MyCardsFragment : Fragment(),
    Injectable,
    CustomViewCallBack,
    CustomViewScrollCallBack,
    AdapterCallBack,
    InternetConnection {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen
    private lateinit var adapter: MyCardsAdapter
    private var binding by autoCleared<FragmentMyCardsBinding>()
    private lateinit var viewModel: MyCardsViewModel
    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView
    var currnetPage = 1
    var startPage = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_cards,
            container, false
        )
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            MyCardsViewModel::class.java
        )
        adapter = MyCardsAdapter(appExecutors)
        binding.toolbar.setTitle(resources.getString(R.string.my_cards))
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)
        setupCustomRecyclerView()
        skeletonScreen = Skeleton.bind(recyclerView)
            .adapter(adapter)
            .load(R.layout.adapter_my_cards_skeleton)
            .count(4)
            .shimmer(true)
            .show()
        getCards(startPage)
//        if(!Preferences.getUserHasCard(requireContext())){
//            binding.buttonSingle.visibility = View.GONE
//        }
        binding.buttonSingle.setOnClickListener {
            findNavController().navigate(MyCardsFragmentDirections.addCard())
        }
    }
    private fun getCards(page: Int) {
//        if (page == 1)
//            viewModel.refresh()
//        else
//            customRecyclerView.setStatus(ListStatus.LOADING_BOTTOM)
        viewModel.refresh()
        viewModel.getCards.observe(this, Observer { data ->
            data.handle(this, activity, skeletonScreen) {it,code->
                binding.buttonLayout.visibility = View.VISIBLE
                if (it.userHasCard) {
                    customRecyclerView.setSwipeRefreshStatus(true)
                    adapter.notifyDataSetChanged()
                    customRecyclerView.setStatus(ListStatus.SUCCESS)
                    adapter.submitList(it.cards)
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noDataFoundLayout.visibility = View.GONE
                    binding.buttonLayout.visibility = View.GONE
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.noDataFoundLayout.visibility = View.VISIBLE
                    binding.buttonLayout.visibility = View.VISIBLE
                    binding.noDataFoundLayout.image_view.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_no_card)
                    )
                    binding.noDataFoundLayout.title.text = getString(R.string.no_card_found)
                    binding.noDataFoundLayout.sub_title.text =
                        getString(R.string.no_card_found_desc)
                }
            }
        })
    }
    private fun setupCustomRecyclerView() {
        customRecyclerView = binding.recyclerView
        recyclerView = customRecyclerView.getRecyclerView()!!
        customRecyclerView.setCustomViewCallBack(this)
        customRecyclerView.setCustomViewScrollCallBack(this)
        recyclerView?.adapter = adapter
        adapter.setAdapterCallBack(this)
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
    override fun onDestroy() {
        super.onDestroy()
        viewModel.getCards.removeObservers(this)
    }
    override fun onScrollChange(scrollDirection: ScrollDirection?) {
    }
    override fun onRetryClicked() {
        getCards(startPage)
    }
    override fun onRefresh(page: Int) {
        viewModel.getCards.removeObservers(viewLifecycleOwner)
        getCards(page)
    }
    override fun showNoConnection() {
        binding.buttonLayout.visibility = View.GONE
        adapter.notifyDataSetChanged()
        customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)
    }
    override fun richToEnd() {
//        currnetPage += 1
//        getCards(currnetPage)
    }
}