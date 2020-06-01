package com.baman.manex.ui.burn

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.data.dto.ShopResultDto
import com.baman.manex.data.dto.VoucherResultDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.databinding.FragmentBurnBinding
import com.baman.manex.databinding.FragmentMainCollapseBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.CollapsingToolbarFragment
import com.baman.manex.ui.common.BannerAdapter
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.autoCleared
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_earn.*
import kotlinx.android.synthetic.main.layout_pin_main_toolbar.*
import kotlinx.android.synthetic.main.layout_ticket_full.view.*
import javax.inject.Inject

class BurnFragment : CollapsingToolbarFragment(), Injectable, InternetConnection,
    CustomViewCallBack {

    private lateinit var skeletonScreen: ViewSkeletonScreen
    private lateinit var bannerSkeletonScreen: RecyclerViewSkeletonScreen

    private var contentLayoutBinding by autoCleared<FragmentBurnBinding>()
    private var collapsingLayoutBinding by autoCleared<FragmentMainCollapseBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var viewModel: BurnFragmentViewModel
    private var manexStoreAdapter by autoCleared<ManexStoreAdapter>()
    private var voucherAdapter by autoCleared<VoucherAdapter>()
    private var isInitializedShop = false
    private var isInitializedVoucher = false
    private var isInitializedNoConnection = false
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var pinToolbar: View
    private var collapsed = false
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        contentLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_burn, container, false
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
                R.drawable.ic_burn_collapse,
                null
            )
        ).into(collapsingLayoutBinding.imageView)
        return collapsingLayoutBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isInitializedShop = false
        isInitializedVoucher = false
        isInitializedNoConnection = false

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(BurnFragmentViewModel::class.java)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        collapsingLayoutBinding.background.setBackgroundResource(R.drawable.burn_header)

        pinToolbar.title.text = requireContext().getString(R.string.bottom_nav_item_burn)
        pinToolbar.title.visibility = View.VISIBLE

        tabbedlist.setExpandableRoot(this)

        tabbedlist.setupRecyclerCallback(this)


        manexStoreAdapter =
            ManexStoreAdapter(context!!) {
                findNavController().navigate(
                    BurnFragmentDirections.showStoreManexDetail(
                        it
                    )
                )
            }
        voucherAdapter = VoucherAdapter(context!!) {
            findNavController().navigate(
                BurnFragmentDirections.voucherDetail(
                    it
                )
            )
        }

        if (getToolbar() != null)
            ViewCompat.setElevation(getToolbar(), 0f)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getAppBar().outlineProvider = null
        }

        bannerAdapter = BannerAdapter(appExecutors)

        skeletonScreen = Skeleton.bind(contentLayoutBinding.root)
            .load(R.layout.fragment_burn_skeleton)
            .shimmer(true)
            .show()

        bannerSkeletonScreen = Skeleton.bind(collapsingLayoutBinding.recyclerView)
            .adapter(bannerAdapter)
            .load(R.layout.adapter_banner_skeleton)
            .shimmer(true)
            .show()

        getManexCount()

        getVoucherData()

        setupRecycler(collapsingLayoutBinding.recyclerView)

        getBannerData()
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

    private fun getShopData() {
        viewModel.refresh()
        viewModel.shopData.observe(this, Observer { res ->
            res.handle(this, activity, null, skeletonScreen) { res, code ->

                initializeTabbedShop(res)

                tabbedlist.setupRecyclerSuccess()

                manexStoreAdapter.notifyDataSetChanged()

                res.data[0].shops.let {
                    manexStoreAdapter.setData(it)
                }

            }
        })
    }

    private fun getVoucherData() {
        viewModel.refreshVoucher()
        viewModel.voucherData.observe(this, Observer {
            it.handle(this, requireActivity(), null, null,
                null, null, null, { msg, code ->

                    getShopData()

                }, { res, code ->

                    getShopData()

                    initializeTabbedVoucher(res)

                    tabbedlist.setupRecyclerSuccess()

                    voucherAdapter.notifyDataSetChanged()

                    res.data[0].vouchers.let {
                        voucherAdapter.setData(it)
                    }
                })
        })

    }

    private fun getBannerData() {
        viewModel.bannerData(RequestType.Burn).observe(this, Observer {
            it.handle(this, requireActivity(), bannerSkeletonScreen) { it, code ->
                bannerAdapter.submitList(it[0].bannerDetails)
            }
        })
    }

    private fun setupRecycler(recyclerView: RecyclerView) {
        if (recyclerView.onFlingListener != null)
            return
        val hPadding = resources.getDimensionPixelSize(R.dimen.burn_slider_horizontalmargin)
        recyclerView.clipToPadding = false
        recyclerView.setPadding(hPadding, 0, hPadding, 0)

        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(recyclerView)

        showRecyclerView(null != bannerAdapter)

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

    private fun initializeTabbedVoucher(res: VoucherResultDto) {
        if (isInitializedVoucher) return
        isInitializedVoucher = true
        var showMore = res.data[0].showMore
        tabbedlist.addItemGroup(res.data[0].title, voucherAdapter, showMore) {
            findNavController().navigate(
                BurnFragmentDirections.burnVoucher(
                    null
                )
            )
        }

    }

    private fun initializeTabbedShop(res: ShopResultDto) {
        if (isInitializedShop) return
        isInitializedShop = true
        var showMore = res.data[0].showMore
        tabbedlist.addItemGroup(res.data[0].title, manexStoreAdapter, true) {
            findNavController().navigate(
                BurnFragmentDirections.manexStore(
                    null
                )
            )
        }
    }

    override fun showNoConnection() {
        voucherAdapter.notifyDataSetChanged()
        manexStoreAdapter.notifyDataSetChanged()

        initializeTabbedListForNoConnection()

        tabbedlist.setupRecyclerFailed()
    }

    override fun onRetryClicked() {
        getBannerData()
        getVoucherData()
    }

    override fun onRefresh(page: Int) {
        getBannerData()
        getVoucherData()
    }

    private fun initializeTabbedListForNoConnection() {

        if (isInitializedVoucher || isInitializedShop)
            return
        else {
            isInitializedVoucher = true
            isInitializedShop = true
            tabbedlist.addItemGroup("کارت هدیه", voucherAdapter, false) {
                findNavController().navigate(
                    BurnFragmentDirections.burnVoucher(
                        null
                    )
                )
            }
            tabbedlist.addItemGroup("فروشگاه منکس", manexStoreAdapter, false) {
                findNavController().navigate(
                    BurnFragmentDirections.manexStore(
                        null
                    )
                )
            }

            showRecyclerView(false)
        }
    }
}