package com.baman.manex.ui.home


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
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
import com.baman.manex.data.dto.ManexStoreInsideDto
import com.baman.manex.databinding.FragmentHomeBinding
import com.baman.manex.databinding.FragmentHomeCollapseBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.CollapsingToolbarFragment
import com.baman.manex.ui.home.exoVideoPlayer.VideoPlayerActivity
import com.baman.manex.ui.common.BannerAdapter
import com.baman.manex.util.*
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.control_home_list_header.view.*
import kotlinx.android.synthetic.main.layout_pin_main_toolbar.*
import kotlinx.android.synthetic.main.layout_ticket_full.view.*
import kotlinx.android.synthetic.main.layout_ticket_full.view.image_ticket
import kotlinx.android.synthetic.main.layout_ticket_full.view.ticketView
import kotlinx.android.synthetic.main.layout_ticket_full.view.title
import kotlinx.android.synthetic.main.layout_ticket_mid.view.*
import java.lang.StringBuilder
import javax.inject.Inject

class HomeFragment : CollapsingToolbarFragment(), Injectable {


    private lateinit var skeletonScreen: ViewSkeletonScreen
    private lateinit var bannerSkeletonScreen: RecyclerViewSkeletonScreen


    private var contentLayoutBinding by autoCleared<FragmentHomeBinding>()
    private var collapsingLayoutBinding by autoCleared<FragmentHomeCollapseBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var manexStoreOfferAdapter: ManexStoreOfferAdapter

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var viewModel: HomeViewModel
    private lateinit var pinToolbar: View


    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        contentLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
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
            R.layout.fragment_home_collapse, container, false
        )
        return collapsingLayoutBinding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(HomeViewModel::class.java)

        //get userHasCard Info and save to Preferences

        viewModel.userHasCard().observe(this, Observer {
            it.handle(HomeFragment(), activity) {it,code->
                Preferences.setUserHasCard(context!!, it.userHasCard)
                if (Preferences.getUserHasCard(requireContext())) {
                    contentLayoutBinding.addCardSuggestionLinear.visibility = View.GONE
                }
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pinToolbar.title.text = requireContext().getString(R.string.bottom_nav_item_main)
        pinToolbar.title.visibility = View.VISIBLE

        if (getToolbar() != null)
            ViewCompat.setElevation(getToolbar(), 0f)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getAppBar().outlineProvider = null
        }

        val font = ResourcesCompat.getFont(requireActivity(), R.font.iranyekan)
        val searchAutoComplete =
            collapsingLayoutBinding.search
                .findViewById<View>(R.id.search_src_text) as SearchView.SearchAutoComplete

        searchAutoComplete.typeface = font
        searchAutoComplete.textSize = 12f

        getManexCount()

        getBannerData()

        bannerAdapter = BannerAdapter(appExecutors)

        manexStoreOfferAdapter =
            ManexStoreOfferAdapter(
                appExecutors,
                itemClickCallback = {})

        handleClicks()

        skeletonScreen = Skeleton.bind(contentLayoutBinding.root)
            .load(R.layout.fragment_home_skeleton)
            .shimmer(true)
            .show()

        bannerSkeletonScreen = Skeleton.bind(collapsingLayoutBinding.recyclerView)
            .adapter(bannerAdapter)
            .load(R.layout.adapter_banner_skeleton)
            .shimmer(true)
            .show()


        Handler(Looper.getMainLooper()).postDelayed({

            setupManexStoreRecycle(contentLayoutBinding.recyclerViewManexStore)
            setupRecycler(collapsingLayoutBinding.recyclerView)
            skeletonScreen.hide()
            bannerSkeletonScreen.hide()

        }, 1500)

        setTicketData()

    }

    private fun handleClicks() {
        contentLayoutBinding.videoPlayer.setOnClickListener {
            startActivity(Intent(requireActivity(), VideoPlayerActivity::class.java))
        }

        contentLayoutBinding.addCardHome.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.addCard())
        }

        contentLayoutBinding.addCardHomeRight.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.addCard())
        }

        contentLayoutBinding.addCardHomeLeft.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.addCard())
        }

        contentLayoutBinding.card7241.setOnClickListener {
            PublicFunction.openBrowserDialog(
                requireActivity(),
                "https://webapp.724.ir/App/Charge"
            )
        }

        contentLayoutBinding.card7242.setOnClickListener {
            PublicFunction.openBrowserDialog(
                requireActivity(),
                "https://webapp.724.ir/App/Bill"
            )
        }

        contentLayoutBinding.card7243.setOnClickListener {
            PublicFunction.openBrowserDialog(
                requireActivity(),
                "https://webapp.724.ir/App/Internet"
            )
        }

        contentLayoutBinding.manexPlusCard.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.manexPlus())
        }
        contentLayoutBinding.manexPlusCard2.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.manexPlus())
        }
        contentLayoutBinding.manexPlusCard3.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.manexPlus())
        }

        contentLayoutBinding.offerVoucher.show_more_text.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.burnVoucher(
                    null
                )
            )
        }

        contentLayoutBinding.offerManexStore.show_more_text.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.manexStore(
                    null
                )
            )
        }

        contentLayoutBinding.offerManexPlus.show_more_text.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.manexPlus())
        }

        contentLayoutBinding.lastShopping.show_more_text.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.lastShopping())
        }

        contentLayoutBinding.lastTransaction.show_more_text.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.lastTransaction())
        }

        collapsingLayoutBinding.searchLinear.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeSearch())
        }

    }

    private fun setTicketData() {

        val typeface = ResourcesCompat.getFont(context!!, R.font.iranyekan_regular)

        //set add card ticket
        contentLayoutBinding.addCardHomeRight.image_ticket.setImageDrawable(
            VectorDrawableCompat.create(
                resources,
                R.drawable.ic_add_card_tap30,
                null
            )
        )
        contentLayoutBinding.addCardHomeRight.ticketView.backgroundColor =
            resources.getColor(R.color.tap30_ticket)
        contentLayoutBinding.addCardHomeRight.title_ticket.text = "کارت هدیه تپسی"

        val amountTap30 = "۱۲ هزار تومان"
        val text = getString(R.string.tap30_amount, amountTap30)
        val ssb = SpannableStringBuilder(text)
        val startingIndex = text.indexOf(amountTap30)
        ssb.setSpan(
            CustomTypefaceSpan("", typeface),
            startingIndex, startingIndex + amountTap30.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        contentLayoutBinding.addCardHomeRight.sub_title_ticket.text = ssb
        contentLayoutBinding.addCardHomeRight.pay_ticket.text = "پرداخت ۲ هزار تومان"

        contentLayoutBinding.addCardHomeLeft.image_ticket.setImageDrawable(
            VectorDrawableCompat.create(
                resources,
                R.drawable.ic_add_card_iran_ticket,
                null
            )
        )
        contentLayoutBinding.addCardHomeLeft.ticketView.backgroundColor =
            resources.getColor(R.color.iran_tick_ticket)
        contentLayoutBinding.addCardHomeLeft.title_ticket.text = "کارت هدیه ایران تیک"

        val amountTick = "۱۵ هزار تومان"
        val textTick = getString(R.string.tick_amount, amountTick)
        val ssbTick = SpannableStringBuilder(textTick)
        val startingIndexTick = textTick.indexOf(amountTick)
        ssbTick.setSpan(
            CustomTypefaceSpan("", typeface),
            startingIndexTick, startingIndexTick + amountTick.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        contentLayoutBinding.addCardHomeLeft.sub_title_ticket.text = ssbTick
        contentLayoutBinding.addCardHomeLeft.pay_ticket.text = "پرداخت ۵ هزار تومان"
        //

        //set manex plus ticket data
        contentLayoutBinding.manexPlusCard.image_ticket.setImageDrawable(resources.getDrawable(R.drawable.manex_plus_tap))
        contentLayoutBinding.manexPlusCard.ticketView.backgroundColor =
            resources.getColor(R.color.tap30_ticket_home)
        contentLayoutBinding.manexPlusCard.title.text = resources.getString(R.string.tap30)
        contentLayoutBinding.manexPlusCard.sub_title.text = "۱۵ سفر درون شهری در یک ماه"
        contentLayoutBinding.manexPlusCard.pay_text.text = "کسب ۱۰۰ منکس هدیه"

        contentLayoutBinding.manexPlusCard3.image_ticket.setImageDrawable(resources.getDrawable(R.drawable.manex_plus_korosh))
        contentLayoutBinding.manexPlusCard3.ticketView.backgroundColor =
            resources.getColor(R.color.ok_ticket_home)
        contentLayoutBinding.manexPlusCard3.title_ticket.text = resources.getString(R.string.ok)
        contentLayoutBinding.manexPlusCard3.sub_title_ticket.text =
            "حداقل ۵۰۰ هزار تومان خرید در دیماه ۹۸"
        contentLayoutBinding.manexPlusCard3.pay_ticket.text = "کسب ۲۵۰ منکس بیشتر"

        contentLayoutBinding.manexPlusCard2.image_ticket.setImageDrawable(resources.getDrawable(R.drawable.manex_plus_alibaba))
        contentLayoutBinding.manexPlusCard2.ticketView.backgroundColor =
            resources.getColor(R.color.alibaba_ticket_home)
        contentLayoutBinding.manexPlusCard2.title_ticket.text =
            resources.getString(R.string.alibaba)
        contentLayoutBinding.manexPlusCard2.sub_title_ticket.text =
            "خرید تور استانبول برای دو نفر در بهمن ماه ۹۸"
        contentLayoutBinding.manexPlusCard2.title_ticket.setTextColor(resources.getColor(R.color.black))
        contentLayoutBinding.manexPlusCard2.sub_title_ticket.setTextColor(resources.getColor(R.color.black))
        contentLayoutBinding.manexPlusCard2.pay_ticket.setTextColor(resources.getColor(R.color.black))
        contentLayoutBinding.manexPlusCard2.pay_ticket.text = "کسب ۱۰۰ منکس بیشتر"
        //
    }

    private fun getManexCount() {
        viewModel.walletData.observe(this, Observer { res ->
            res.handle(this, requireActivity()) {it,code->
                var stringBuilder = StringBuilder()
                stringBuilder.append(it.manexAmount.toInt().toString().toPersianNumber())
                    .append(" ").append("مَنِکس")
                collapsingLayoutBinding.textManexcount.text = stringBuilder.toString()
                text_manexcount_top.text = it.manexAmount.toInt().toString()
            }
        })
    }

    private fun setupManexStoreRecycle(recyclerViewManexStore: RecyclerView) {
        recyclerViewManexStore.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )

        recyclerViewManexStore.adapter = manexStoreOfferAdapter

        setMockDataToList()

    }

    private fun getBannerData() {
        viewModel.bannerData.observe(this, Observer {
            it.handle(this, requireActivity(), bannerSkeletonScreen) {it,code->
                it[0].bannerDetails
                bannerAdapter.submitList(it[0].bannerDetails)
            }
        })
    }

    private fun setMockDataToList() {

        var list = mutableListOf<ManexStoreInsideDto>()
        list.add(
            ManexStoreInsideDto(
                100, 50, 1, "https://dev3.web.manexapp.com/external.jpg",
                "", "مدل My Passport WDBYNN", "هارد اکسترنال وسترن"
            )
        )
        list.add(
            ManexStoreInsideDto(
                100, 250, 2, "https://dev3.web.manexapp.com/external.jpg",
                "", "مدل My Passport WDBYNN", "هارد اکسترنال وسترن"
            )
        )
        list.add(
            ManexStoreInsideDto(
                100, 150, 3, "https://dev3.web.manexapp.com/external.jpg",
                "", "مدل My Passport WDBYNN", "هارد اکسترنال وسترن"
            )
        )
        list.add(
            ManexStoreInsideDto(
                100, 350, 4, "https://dev3.web.manexapp.com/external.jpg",
                "", "مدل My Passport WDBYNN", "هارد اکسترنال وسترن"
            )
        )

        manexStoreOfferAdapter.submitList(list)

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


}
