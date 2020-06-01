package com.baman.manex.ui.onlineService.detail

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
import com.baman.manex.R
import com.baman.manex.data.dto.ManexConditionsDto
import com.baman.manex.data.dto.LikeDto
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.data.dto.StoreInfoDto
import com.baman.manex.databinding.FragmentOnlineservicedetailBinding
import com.baman.manex.di.Injectable
import com.baman.manex.di.ViewModelFactory
import com.baman.manex.ui.baseClass.RetryCallBack
import com.baman.manex.ui.baseClass.ServiceDetailFragment
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.autoCleared
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class OnlineServiceDetailFragment : ServiceDetailFragment(), Injectable,
    InternetConnection, RetryCallBack {

    private var containerLayoutBinding by autoCleared<FragmentOnlineservicedetailBinding>()

    val args: OnlineServiceDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: OnlineServiceDetailViewModel

    private lateinit var onlineStore: StoreInfoDto

    private var likeStatus = false

    private var isEarn = false

    private lateinit var skeletonScreen: ViewSkeletonScreen

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        containerLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_onlineservicedetail, container, false
        )
        return containerLayoutBinding.root
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(OnlineServiceDetailViewModel::class.java)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRetryCallBack(this)

        onlineStore = args.storeInfo
        isEarn = args.isEarn

        skeletonScreen = Skeleton.bind(containerLayoutBinding.root)
            .load(R.layout.fragment_onlineservicedetail_skeleton)
            .shimmer(true)
            .show()

        getBranchDetails()
        getManexCount()
    }

    private fun getBranchDetails() {
        viewModel.getBranchDetail(onlineStore.id).observe(this, Observer { it ->
            it.handle(
                OnlineServiceDetailFragment(),
                activity,
                null,
                skeletonScreen,
                getSkeletonTop()
            ) {it,code->
                dataLayoutVisibility()
                bindUi(it)
            }
        })
    }

    private fun bindUi(storeInfo: StoreInfoDto) {
        containerLayoutBinding.storeInfo = storeInfo
        containerLayoutBinding.isEarn = isEarn

        val imagePath = storeInfo.imagePath

        if (imagePath.isNotBlank()) {
            GlideApp.with(this)
                .load(imagePath)
                .into(getIconImageView())
        }


        if (isEarn) {
            getTitleTextView().text = storeInfo.earnManexConditionTitle.title.toPersianNumber()
            getDescriptionTextView().text =
                getDescriptionSecondPart(storeInfo.earnManexConditionTitle)
        } else {
            getearnLinear().visibility = View.GONE
            getBurnLinear().visibility = View.VISIBLE
            getBurnTitleTextView().text = storeInfo.burnManexConditionTitle.title.toPersianNumber()
            getBurnDescriptionTextView().text =
                getDescriptionSecondPart(storeInfo.burnManexConditionTitle)
        }


        if (isEarn) {
            if (storeInfo.earnManexConditions.isNotEmpty()) {
                containerLayoutBinding.containerConditions.setItem(
                    storeInfo.earnManexConditions,
                    isEarn
                )
            } else {
                containerLayoutBinding.textContitionstitle.visibility = View.GONE
                containerLayoutBinding.containerConditions.visibility = View.GONE
                containerLayoutBinding.dividerConditions.visibility = View.GONE

            }
        } else {
            if (storeInfo.burnManexConditions.isNotEmpty()) {
                containerLayoutBinding.containerConditions.setItem(
                    storeInfo.burnManexConditions,
                    isEarn
                )
            } else {
                containerLayoutBinding.textContitionstitle.visibility = View.GONE
                containerLayoutBinding.containerConditions.visibility = View.GONE
                containerLayoutBinding.dividerConditions.visibility = View.GONE

            }
        }

        val text = getString(R.string.onlineservicedetail_button_format, storeInfo.name)
        setButtonSingle(text) {

            viewModel.getGoLink(storeInfo.partnerId).observe(this, Observer {
                it.handle(
                    this, requireActivity(), null,
                    null,
                    null, getButtonSingleText(), getButtonSingleLoading()
                ) {it,code->

                    PublicFunction.openBrowserDialog(requireActivity(), it.link)
                }
            })

        }

        //set top data
        setBackgroundColor(storeInfo.backgroundColor!!)
        setCollapsingTextColor(storeInfo.isBlackTheme)
        GlideApp.with(context!!).load(storeInfo.imagePath).into(getIconImageView())

        getToolbar().setTitle(storeInfo.shortName)
        getToolbar().showUpIcon(true) { findNavController().navigateUp() }

        setBaseStatusColor(storeInfo.statusColor)

        likeStatus = storeInfo.likeStatus
        setLikeVisibility(storeInfo.likeStatus)
    }

    private fun getDescriptionSecondPart(condition: ManexConditionsDto): String {
        val earnManex = condition.manexCount.toString().toPersianNumber()
        return if (isEarn) {
            getString(
                R.string.onlineservicedetail_description_secondpart_earn_format,
                earnManex
            )
        } else {
            getString(R.string.onlineservicedetail_description_secondpart_format, earnManex)
        }
    }

    override fun onLikeIconClicked(v: View) {
        likeStatus = !likeStatus
        setLikeVisibility(likeStatus, true)

        var branchLikeDto = LikeDto(onlineStore.id)

        viewModel.setVoucherLikeData(branchLikeDto)
        viewModel.setVoucherLike.observe(this, Observer {
            it.handle(OnlineServiceDetailFragment(), requireActivity()) { it,code->
                it
            }
        })
    }

    override fun onShareIconClicked(v: View) {
        PublicFunction.shareLinkDialog(requireContext(), "www.google.com")
    }

    private fun getManexCount() {
        viewModel.refresh()
        viewModel.walletData.observe(this, Observer { res ->
            res.handle(this, requireActivity()) {it,code->
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