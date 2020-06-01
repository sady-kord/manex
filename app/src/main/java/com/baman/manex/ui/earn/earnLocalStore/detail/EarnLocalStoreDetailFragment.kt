package com.baman.manex.ui.earn.earnLocalStore.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.baman.manex.R
import com.baman.manex.controls.CountLabelType
import com.baman.manex.controls.LocationDetailsControl
import com.baman.manex.data.dto.ManexConditionsDto
import com.baman.manex.data.dto.StoreInfoDto
import com.baman.manex.data.model.ContactType
import com.baman.manex.data.model.RequestType
import com.baman.manex.databinding.FragmentEarnlocalstoredetailBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseActivity
import com.baman.manex.ui.baseClass.RetryCallBack
import com.baman.manex.ui.baseClass.ServiceDetailFragment
import com.baman.manex.util.*
import com.baman.manex.util.glide.GlideApp
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.master.permissionhelper.PermissionHelper
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.control_location_details.view.*
import kotlinx.android.synthetic.main.fragment_collapsingtoolbar.*
import kotlinx.android.synthetic.main.fragment_earnlocalstoredetail.*
import timber.log.Timber
import javax.inject.Inject

class EarnLocalStoreDetailFragment : ServiceDetailFragment(), Injectable,
    InternetConnection, RetryCallBack {

    private lateinit var onlineStore: StoreInfoDto

    private var containerLayoutBinding by autoCleared<FragmentEarnlocalstoredetailBinding>()

    private lateinit var viewModel: EarnLocalStoreDetailViewModel

    private var likeStatus = false
    private var userHasCard = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var skeletonScreen: ViewSkeletonScreen

    val args: EarnLocalStoreDetailFragmentArgs by navArgs()

    var changePositionWithScroll: Boolean = false

    var userSelectTabLayout: Boolean = false

    var conditionsY: Int = 0
    var othersY: Int = 0
    var nearsY: Int = 0

    var source: String = ""

    private lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        containerLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_earnlocalstoredetail, container, false
        )
        return containerLayoutBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(EarnLocalStoreDetailViewModel::class.java)

        setRetryCallBack(this)

        onlineStore = args.store

        mMapView = containerLayoutBinding.mapView

        mMapView.onCreate(savedInstanceState)

        if (!args.source.isNullOrEmpty())
            source = args.source!!

        skeletonScreen = Skeleton.bind(containerLayoutBinding.root)
            .load(R.layout.fragment_earnlocalstoredetail_skeleton)
            .shimmer(true)
            .show()

        getBranchDetails()
        getManexCount()


    }


    private fun getLocationPermission(
        latitude: Double,
        longitude: Double,
        shortName: String,
        name1: String
    ) {
        val permissionHelper = (context as BaseActivity)
            .getPermissionHelper(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 100
            )

        permissionHelper.request(object : PermissionHelper.PermissionCallback {
            override fun onPermissionGranted() {
                initMap(latitude, longitude, shortName, name1)

            }

            override fun onIndividualPermissionGranted(strings: Array<String>) {
                initMap(latitude, longitude, shortName, name1)
            }

            override fun onPermissionDenied() {

            }

            override fun onPermissionDeniedBySystem() {
            }
        })
    }

    fun initMap(
        latitude: Double,
        longitude: Double,
        shortName: String,
        name1: String
    ) {

        mMapView.onResume()
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView.getMapAsync { mMap ->
            googleMap = mMap
            googleMap.isMyLocationEnabled = true

            val sydney = LatLng(latitude, longitude)
            googleMap.addMarker(MarkerOptions().position(sydney).title(shortName).snippet(name1))

            val cameraPosition =
                CameraPosition.Builder().target(sydney).zoom(14f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            var setting = googleMap.uiSettings
            setting.isCompassEnabled = false
            setting.isMapToolbarEnabled = false
            setting.isMyLocationButtonEnabled = false
            setting.isScrollGesturesEnabled = false
            setting.isScrollGesturesEnabledDuringRotateOrZoom = false
            setting.isTiltGesturesEnabled = false
            setting.isZoomControlsEnabled = false
            setting.isZoomGesturesEnabled = false

        }
    }


    private fun getBranchDetails() {
        viewModel.getBranchDetail(onlineStore.id, source).observe(this, Observer { it ->
            it.handle(
                this, activity, null, skeletonScreen
                , getSkeletonTop()
            ) { it, code ->
                dataLayoutVisibility()
                bindUi(it)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun bindUi(storeInfo: StoreInfoDto) {

        containerLayoutBinding.storeInfo = storeInfo

        val imagePath = storeInfo.imagePath
        if (imagePath.isNotBlank()) {
            GlideApp.with(this)
                .load(imagePath)
                .into(getIconImageView())
        }

        containerLayoutBinding.locationControl.setLocationData(storeInfo)

        val conditions = storeInfo.earnManexConditions

        if (conditions.isNotEmpty()) {
            val firstCondition = conditions[0]
            getTitleTextView().text = firstCondition.title.toPersianNumber()
            getDescriptionTextView().text = getDescriptionSecondPart(firstCondition)
            getManexCountControl().setManexCount(firstCondition.manexCount)
        }

        getLocationPermission(
            storeInfo.latitude,
            storeInfo.longitude,
            storeInfo.shortName,
            storeInfo.name
        )

        if (!storeInfo.otherBranchs.isNullOrEmpty()) {
            containerLayoutBinding.branchItems.setItem(storeInfo.otherBranchs)
        } else {
            containerLayoutBinding.otherBranchLinear.visibility = View.GONE
        }

        containerLayoutBinding.nearByStore.clearItem()

        if (!storeInfo.nearBranchs.isNullOrEmpty()) {
            containerLayoutBinding.nearByStore.setItem(storeInfo.similarBranches)
        } else {
            containerLayoutBinding.nearbranchLinear.visibility = View.GONE
        }



        if (!storeInfo.otherBranchs.isNullOrEmpty() || !storeInfo.nearBranchs.isNullOrEmpty())
            setupTabLayout(storeInfo)
        else
            tablayout.visibility = View.GONE



        userHasCard = Preferences.getUserHasCard(context!!)

        if (userHasCard) {
            setButtonDouble({
                //call

                PublicFunction.actionCall(context!!, "02188985421")

            }, {
                //direction
                PublicFunction.shareLocationDialog(
                    context!!,
                    storeInfo.latitude,
                    storeInfo.longitude
                )
            })
        } else {
            val text = getString(R.string.add_card_for_earn_manex)
            setButtonSingle(text) {
                findNavController().navigate(EarnLocalStoreDetailFragmentDirections.addCard())
            }
        }

        //set top data
        setBackgroundColor(storeInfo.backgroundColor!!)
        setCollapsingTextColor(storeInfo.isBlackTheme)

        getManexCountControl().setEntry(
            RequestType.Earn, CountLabelType.Collapse,
            storeInfo.isBlackTheme
        )

        getToolbar().setTitle(storeInfo.shortName)
        getToolbar().showUpIcon(true) { findNavController().navigateUp() }

        setBaseStatusColor(storeInfo.statusColor)

        likeStatus = storeInfo.likeStatus
        setLikeVisibility(storeInfo.likeStatus)

        containerLayoutBinding.locationControl.setOnClickListener(
            object : LocationDetailsControl.OnShowMoreLocationClick {
                override fun onClick() {
                    setTopLayouts()
                }

                override fun onContactClick(contactType: String, value: String) {
                    when (ContactType.Parse(contactType)) {
                        ContactType.Phone, ContactType.Mobile -> {
                            requireContext().startActivity(
                                Intent(
                                    Intent.ACTION_DIAL,
                                    Uri.fromParts("tel", value.replace(" ", ""), null)
                                )
                            )
                        }
                        ContactType.SiteAddress, ContactType.Instagram -> {
                            PublicFunction.openBrowserDialog(requireActivity(), value)
                        }
                    }
                }
            })

        containerLayoutBinding.locationControl.image_map.setOnClickListener {
            PublicFunction.shareLocationDialog(
                requireContext(),
                storeInfo.latitude,
                storeInfo.longitude
            )
        }

        containerLayoutBinding.showMoreNearBranch.setOnClickListener {
            findNavController().navigate(
                EarnLocalStoreDetailFragmentDirections.showMoreNearBranch(
                    storeInfo
                )
            )
        }

        containerLayoutBinding.showMoreBranch.setOnClickListener {

            findNavController().navigate(
                EarnLocalStoreDetailFragmentDirections.otherBranch(
                    storeInfo
                )
            )
        }

    }

    fun setTopLayouts() {

        var tabLayoutHeight = resources.getDimensionPixelSize(R.dimen.tablayout_height)

        Handler(Looper.getMainLooper()).post {
            conditionsY = condition_Linear.top - tabLayoutHeight
            othersY = otherBranch_Linear.top - tabLayoutHeight
            nearsY = nearbranch_Linear.top - tabLayoutHeight
            Timber.d("cond y: $conditionsY")
            Timber.d("others y: $othersY")
            Timber.d("nears y: $nearsY")
        }

    }

    private fun setupTabLayout(storeInfo: StoreInfoDto) {

        val conditionsTab = getTabLayout().newTab()
        val otherBranchesTab = getTabLayout().newTab()
        val nearBranchesTab = getTabLayout().newTab()

        val selectedTabPrevious = getTabLayout().selectedTabPosition

        if (getTabLayout().tabCount > 0) {
            getTabLayout().removeAllTabs()
            getTabLayout().clearOnTabSelectedListeners()
            getTabLayout().selectTab(nearBranchesTab)
        }

        conditionsTab.text = "شرایط کسب"
        getTabLayout().addTab(conditionsTab)

        if (!storeInfo.otherBranchs.isNullOrEmpty()) {
            otherBranchesTab.text = "سایر شعب"
            getTabLayout().addTab(otherBranchesTab)
        }

        if (!storeInfo.nearBranchs.isNullOrEmpty()) {
            nearBranchesTab.text = "فروشگاه‌های مشابه"
            getTabLayout().addTab(nearBranchesTab)
        }

        if (selectedTabPrevious != -1)
            getTabLayout().selectTab(getTabLayout().getTabAt(selectedTabPrevious))

        setTopLayouts()

        getTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab) {
                    conditionsTab -> {
                        if (!changePositionWithScroll) {
                            userSelectTabLayout = true
                            getNestedScroll().smoothScrollTo(0, conditionsY)
                        } else {
                            changePositionWithScroll = false
                        }
                    }
                    otherBranchesTab -> {
                        if (!changePositionWithScroll) {
                            userSelectTabLayout = true
                            getNestedScroll().smoothScrollTo(0, othersY)
                        } else {
                            changePositionWithScroll = false
                        }
                    }
                    nearBranchesTab -> {
                        if (!changePositionWithScroll) {
                            userSelectTabLayout = true
                            getNestedScroll().smoothScrollTo(0, nearsY)
                        } else {
                            changePositionWithScroll = false
                        }
                    }
                }
            }

        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getNestedScroll().setOnScrollChangeListener { p0, p1, scrollY, p3, p4 ->

                var adjustedScrollY = scrollY
                if (storeInfo.nearBranchs.isNullOrEmpty() && storeInfo.otherBranchs.isNullOrEmpty())
                    getTabLayout().visibility = View.GONE
                else if (!userSelectTabLayout) {

                    if (adjustedScrollY >= conditionsY) {
                        getTabLayout().visibility = View.VISIBLE

                        var tab = getTabLayout().newTab()

                        when {
                            othersY < 0 -> {
                                when {
                                    adjustedScrollY > nearsY -> tab = nearBranchesTab
                                    adjustedScrollY > conditionsY -> tab = conditionsTab
                                    adjustedScrollY > othersY -> tab = otherBranchesTab
                                }
                            }
                            nearsY < 0 -> {
                                when {
                                    adjustedScrollY > othersY -> tab = otherBranchesTab
                                    adjustedScrollY > conditionsY -> tab = conditionsTab
                                    adjustedScrollY > nearsY -> tab = nearBranchesTab
                                }
                            }
                            else -> {
                                when {
                                    adjustedScrollY > nearsY -> tab = nearBranchesTab
                                    adjustedScrollY > othersY -> tab = otherBranchesTab
                                    adjustedScrollY > conditionsY -> tab = conditionsTab
                                }
                            }
                        }

                        if (!tab.isSelected) {
                            changePositionWithScroll = true
                            tab.select()
                        }

                    } else {
                        getTabLayout().visibility = View.GONE
                    }

                } else {
                    when (adjustedScrollY) {
                        nearsY -> userSelectTabLayout = false
                        othersY -> userSelectTabLayout = false
                        conditionsY -> userSelectTabLayout = false
                    }
                }

            }
        }

    }

    private fun getDescriptionSecondPart(condition: ManexConditionsDto): String {
        val earnManex = condition.manexCount.toString().toPersianNumber()
        return getString(R.string.onlineservicedetail_description_secondpart_earn_format, earnManex)
    }

    override fun onLikeIconClicked(v: View) {
        likeStatus = !likeStatus

        setLikeVisibility(likeStatus, true)

        viewModel.setBranchLike(onlineStore.id).observe(this, Observer {
            it.handle(this, requireActivity()) { data, code ->
                data
            }
        })
    }

    override fun onShareIconClicked(v: View) {
        PublicFunction.shareLinkDialog(requireContext(), "www.google.com")
    }

    private fun getManexCount() {
        viewModel.refresh()
        viewModel.walletData.observe(this, Observer { res ->
            res.handle(this, requireActivity()) { it, code ->
                dataLayoutVisibility()
                getManexCountText().text = it.manexAmount.toInt().toString().toPersianNumber()
            }
        })
    }

    override fun showNoConnection() {
        containerLayoutBinding.mainLayout.visibility = View.GONE
        showNoInternetLayout()
    }

    private fun dataLayoutVisibility() {
        containerLayoutBinding.mainLayout.visibility = View.VISIBLE
        hideNoInternetLayout()
    }

    override fun onRetryClicked() {
        getBranchDetails()
        getManexCount()
    }


}