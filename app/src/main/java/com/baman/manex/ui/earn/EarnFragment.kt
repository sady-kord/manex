package com.baman.manex.ui.earn

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.controls.AdapterCallBack
import com.baman.manex.controls.CustomRecyclerView
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.controls.CustomViewScrollCallBack
import com.baman.manex.data.dto.BranchResultDto
import com.baman.manex.data.dto.VoucherInsideDto
import com.baman.manex.data.dto.VoucherResultDto
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.RequestType
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.databinding.FragmentEarnBinding
import com.baman.manex.databinding.FragmentMainCollapseBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.CollapsingToolbarFragment
import com.baman.manex.ui.common.BannerAdapter
import com.baman.manex.ui.earn.earnVoucher.EarnShowMoreVoucherAdapter
import com.baman.manex.util.*
import com.baman.manex.util.glide.GlideApp
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_earn.*
import kotlinx.android.synthetic.main.layout_pin_main_toolbar.*
import kotlinx.android.synthetic.main.layout_ticket_full.view.*
import javax.inject.Inject

class EarnFragment : CollapsingToolbarFragment(), Injectable, InternetConnection,
    CustomViewCallBack,
    AdapterCallBack,
    CustomViewScrollCallBack {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentEarnBinding>()

    companion object {
        private const val QUERY_CHANGE_MIN_INTERVAL = 300L
    }

    private var contentLayoutBinding by autoCleared<FragmentEarnBinding>()
    private var collapsingLayoutBinding by autoCleared<FragmentMainCollapseBinding>()
    private lateinit var skeletonScreen: ViewSkeletonScreen
    private lateinit var bannerSkeletonScreen: RecyclerViewSkeletonScreen
    private lateinit var viewModel: EarnViewModel
    private lateinit var localStoreAdapter: LocalStoreAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var pinToolbar: View
    private var userHasCard = false

    private var isInitializedOffline = false
    private var isInitializedVoucher = false
    private var isInitializedNoConnection = false

    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var voucherAdapter: EarnVoucherAdapter
    private lateinit var voucherShowMoreAdapter: EarnShowMoreVoucherAdapter

    var list = mutableListOf<VoucherInsideDto>()

    var currnetPage = 1
    var loadedPage = 0

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        contentLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_earn, container, false
        )
        return contentLayoutBinding.root
    }

    override fun onCreatePinLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pinToolbar = inflater.inflate(R.layout.layout_pin_main_toolbar, container, false)
        return pinToolbar
    }

    override fun onCreateCollapsingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        collapsingLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_collapse, container, false
        )
        GlideApp.with(collapsingLayoutBinding.root.context).load(
            VectorDrawableCompat.create(
                resources,
                R.drawable.ic_earn_collapse,
                null
            )
        )
            .into(collapsingLayoutBinding.imageView)

        return collapsingLayoutBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isInitializedOffline = false
        isInitializedVoucher = false
        isInitializedNoConnection = false

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(EarnViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        getManexCount()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        collapsingLayoutBinding.background.setBackgroundResource(R.drawable.earn_header)

        pinToolbar.title.text = requireContext().getString(R.string.bottom_nav_item_earn)
        pinToolbar.title.visibility = View.VISIBLE

        userHasCard()

        tabbedlist.setExpandableRoot(this)
        tabbedlist.setupRecyclerCallback(this)

        if (getToolbar() != null)
            ViewCompat.setElevation(getToolbar(), 0f)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getAppBar().outlineProvider = null
        }

        bannerAdapter = BannerAdapter(appExecutors)

        voucherAdapter = EarnVoucherAdapter(context!!) {
            findNavController().navigate(
                EarnFragmentDirections.voucherDetail(
                    it
                )
            )
        }

        voucherShowMoreAdapter = EarnShowMoreVoucherAdapter(appExecutors) {
            findNavController().navigate(
                EarnFragmentDirections.voucherDetail(
                    it!!
                )
            )
        }

        localStoreAdapter =
            LocalStoreAdapter(context!!, {
                findNavController().navigate(
                    EarnFragmentDirections.showLocalStoreDetail(
                        it, null
                    )
                )
            }, {
                findNavController().navigate(EarnFragmentDirections.addCard())
            })


        bannerSkeletonScreen = Skeleton.bind(collapsingLayoutBinding.recyclerView)
            .adapter(bannerAdapter)
            .load(R.layout.adapter_banner_skeleton)
            .shimmer(true)
            .show()

        setupRecycler(collapsingLayoutBinding.recyclerView)

        getBannerData()
        getManexCount()

        //searchview
        contentLayoutBinding.search.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(
                    {
                        if (newText.isNullOrBlank()) {
                            viewModel.setQuery("")
                        } else {
                            if (newText.length > 2) {
                                setLoadingOnSearch(true)
                                currnetPage = 1
                                viewModel.setQuery(newText)

                            }
                        }
                    },
                    QUERY_CHANGE_MIN_INTERVAL
                )
                return true
            }
        })

    }

    fun setLoadingOnSearch(isLoading: Boolean) {

        val closeButtonImage: ImageView =
            contentLayoutBinding.search.findViewById(R.id.search_close_btn) as ImageView

        if (isAdded) {

            var a = LinearLayout.LayoutParams(
                PublicFunction.convertDpToPixels(30f, requireContext()),
                PublicFunction.convertDpToPixels(30f, requireContext())
            )

            a.gravity = Gravity.CENTER_VERTICAL

            closeButtonImage.layoutParams = a


            if (isLoading) {
                GlideApp
                    .with(requireContext())
                    .load(R.drawable.search_loading_gif)
                    .into(closeButtonImage)
                closeButtonImage.isEnabled = false
            } else {
                closeButtonImage.setImageResource(R.drawable.ic_search_close)
                closeButtonImage.isEnabled = true
            }

        } else {
            if (isLoading) {
                closeButtonImage.setImageResource(R.drawable.ic_search_close)
                closeButtonImage.isEnabled = false
            } else {
                closeButtonImage.setImageResource(R.drawable.ic_search_close)
                closeButtonImage.isEnabled = true
            }
        }
    }

    private fun userHasCard() {
        viewModel.userHasCard().observe(this, Observer { res ->
            res.handle(this, activity) { it, code ->
                Preferences.setUserHasCard(context!!, it.userHasCard)
                userHasCard = it.userHasCard
                if (userHasCard) {

                    skeletonScreen = Skeleton.bind(contentLayoutBinding.root)
                        .load(R.layout.fragment_earn_secondary_skeleton)
                        .shimmer(true)
                        .angle(20)
                        .duration(1000)
                        .show()

                    contentLayoutBinding.tabbedlist.visibility = View.VISIBLE
                    getVoucherData(1)
                    getLocalStoreData()
                    contentLayoutBinding.search.visibility = View.GONE
                    contentLayoutBinding.customRecyclerView.visibility = View.GONE
                } else {

                    skeletonScreen = Skeleton.bind(contentLayoutBinding.root)
                        .load(R.layout.fragment_earn_primary_skeleton)
                        .shimmer(true)
                        .angle(20)
                        .duration(1000)
                        .show()

                    setupCustomRecyclerView()
                    currnetPage = 1
                    loadedPage = 0
                    getVoucherData(1)
                    contentLayoutBinding.tabbedlist.visibility = View.GONE
                    contentLayoutBinding.search.visibility = View.VISIBLE
                    contentLayoutBinding.customRecyclerView.visibility = View.VISIBLE

                }
            }
        })
    }

    private fun getManexCount() {
        viewModel.refreshManexCount()
        viewModel.walletData.observe(this, Observer { res ->
            res.handle(this, requireActivity()) { it, code ->
                var amount = it.manexAmount.toInt().toString().toPersianNumber()
                val textAmount = requireContext().getString(
                    R.string.manex_format, amount
                )
                val amountSpan = SpannableStringBuilder(textAmount)

                val startingIndexAmount = textAmount.indexOf(amount)

                val colorSpanAmount = ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )

                amountSpan.setSpan(
                    colorSpanAmount,
                    startingIndexAmount,
                    startingIndexAmount + textAmount.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )

                collapsingLayoutBinding.textManexcount.text =
                    PublicFunction.increaseFontSizeForPath(
                        amountSpan,
                        it.manexAmount.toInt().toString().toPersianNumber(), 2f
                    )
                text_manexcount_top.text = it.manexAmount.toInt().toString().toPersianNumber()
            }
        })
    }

    private fun getVoucherData(page: Int) {

        if (userHasCard) {
            viewModel.refreshVoucher()
        } else {
            if (page == 1) {
                viewModel.setPagingVoucherRequest(1)
            } else {
                customRecyclerView.setStatus(ListStatus.LOADING_BOTTOM)
                viewModel.setPagingVoucherRequest(page)
            }
        }

        viewModel.voucherData.observe(this, Observer { it ->
            it.handle(this, activity, bannerSkeletonScreen, skeletonScreen,
                null, null, null, { msg, code ->
                    setLoadingOnSearch(false)
                }) { it, code ->
                if (userHasCard) {
                    initializeTabbedVoucher(it)
                    tabbedlist.setupRecyclerSuccess()

                    voucherAdapter.notifyDataSetChanged()
                    voucherAdapter.setData(it.data[0].vouchers)

                } else {
                    setLoadingOnSearch(false)

                    customRecyclerView.setSwipeRefreshStatus(true)
                    customRecyclerView.setStatus(ListStatus.SUCCESS)


                    if (!it.data[0].vouchers.isNullOrEmpty()) {
                        if (loadedPage != currnetPage || currnetPage == 1) {
                            loadedPage = currnetPage
                            if (loadedPage == 1) {
                                list = mutableListOf()
                                list.addAll(it.data[0].vouchers)
                                voucherShowMoreAdapter.submitList(list)
                            } else {
                                list.addAll(it.data[0].vouchers)
                                voucherShowMoreAdapter.notifyDataSetChanged()
                            }
                        }
                    }


                }

            }
        })
    }

    private fun getLocalStoreData() {
        viewModel.refresh()
        viewModel.offlineData.observe(this, Observer { res ->
            res.handle(this, activity, bannerSkeletonScreen, skeletonScreen) { it, code ->

                initializeTabbedOffline(it)
                tabbedlist.setupRecyclerSuccess()

                localStoreAdapter.notifyDataSetChanged()
                localStoreAdapter.setData(it.data[0].branches, false)
            }
        })
    }

    private fun getBannerData() {
        viewModel.bannerData(RequestType.Earn).observe(this, Observer {
            it.handle(this, requireActivity(), bannerSkeletonScreen) { it, code ->
                it[0].bannerDetails
                bannerAdapter.submitList(it[0].bannerDetails)
            }
        })
    }

    private fun setupCustomRecyclerView() {

        customRecyclerView = contentLayoutBinding.customRecyclerView
        recyclerView = customRecyclerView.getRecyclerView()!!

        customRecyclerView.setCustomViewCallBack(this)
        customRecyclerView.setCustomViewScrollCallBack(this)
        customRecyclerView.setStatus(ListStatus.SUCCESS)

        voucherShowMoreAdapter.setAdapterCallBack(this)

        recyclerView.adapter = voucherShowMoreAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setupRecycler(recyclerView: RecyclerView) {
        if (recyclerView.onFlingListener != null)
            return
        val hPadding = resources.getDimensionPixelSize(R.dimen.burn_slider_horizontalmargin)
        recyclerView.clipToPadding = false
        recyclerView.setPadding(hPadding, 0, hPadding, 0)

        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(recyclerView)

        showRecyclerView(bannerAdapter != null)

        if (recyclerView.adapter != bannerAdapter) {
            recyclerView.adapter = bannerAdapter
        }
    }

    private fun showRecyclerView(show: Boolean) {
        if (show) {
            collapsingLayoutBinding.recyclerView.visibility = View.VISIBLE
        } else {
            collapsingLayoutBinding.recyclerView.visibility = View.GONE
        }
    }

    private fun initializeTabbedOffline(listDto: BranchResultDto) {
        if (isInitializedOffline) return
        isInitializedOffline = true
        var showMore = listDto.data[0].showMore
        tabbedlist.addItemGroup(listDto.data[0].title, localStoreAdapter, true) {
            findNavController().navigate(EarnFragmentDirections.localStore())
        }
    }

    private fun initializeTabbedVoucher(listDto: VoucherResultDto) {
        if (isInitializedVoucher) return
        isInitializedVoucher = true
        var showMore = listDto.data[0].showMore
        tabbedlist.addItemGroup(listDto.data[0].title, voucherAdapter, true) {
            findNavController().navigate(EarnFragmentDirections.voucherFragment(null))
        }
    }

    override fun showNoConnection() {
        localStoreAdapter.notifyDataSetChanged()

        initializeTabbedListForNoConnection()

        tabbedlist.setupRecyclerFailed()
    }

    override fun onRetryClicked() {
        getBannerData()
        userHasCard()
    }

    override fun onRefresh(page: Int) {
        getBannerData()
        userHasCard()
    }

    private fun initializeTabbedListForNoConnection() {

        if (isInitializedVoucher || isInitializedOffline)
            return
        else {
            isInitializedVoucher = true
            isInitializedOffline = true
            tabbedlist.addItemGroup("فروشگاه حضوری", localStoreAdapter, false) {
                findNavController().navigate(EarnFragmentDirections.localStore())
            }
            tabbedlist.addItemGroup("خدمات آنلاین", voucherAdapter, false) {
                findNavController().navigate(EarnFragmentDirections.localStore())
            }
            showRecyclerView(false)
        }
    }

    override fun onScrollChange(scrollDirection: ScrollDirection?) {
    }

    override fun richToEnd() {
        currnetPage += 1
        getVoucherData(currnetPage)
    }
}