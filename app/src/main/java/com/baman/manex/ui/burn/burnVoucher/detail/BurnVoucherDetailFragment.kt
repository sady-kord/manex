package com.baman.manex.ui.burn.burnVoucher.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andrognito.flashbar.Flashbar
import com.baman.manex.R
import com.baman.manex.controls.CountLabelType
import com.baman.manex.controls.buttonlistsheet.PurchaseBottomSheetDialog
import com.baman.manex.data.model.RequestType
import com.baman.manex.databinding.FragmentVoucherDetailBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.RetryCallBack
import com.baman.manex.ui.baseClass.ServiceDetailFragment
import com.baman.manex.util.*
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.snack.SnackHelper
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_voucher_detail.*
import javax.inject.Inject


class BurnVoucherDetailFragment : ServiceDetailFragment(), Injectable,
    InternetConnection, RetryCallBack {

    private var likeStatus = false
    private var voucherID: Long = 0

    private var contentLayoutBinding by autoCleared<FragmentVoucherDetailBinding>()

    private lateinit var viewModel: BurnVoucherDetailViewModel

    val args: BurnVoucherDetailFragmentArgs by navArgs()

    private var flashbar: Flashbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var height = 86f

    private lateinit var skeletonScreen: ViewSkeletonScreen

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contentLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_voucher_detail, container, false
        )
        return contentLayoutBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(BurnVoucherDetailViewModel::class.java)

        setRetryCallBack(this)

        args.store.id?.let { voucherID = it }

        getManexCount()

        skeletonScreen = Skeleton.bind(contentLayoutBinding.root)
            .load(R.layout.fragment_voucher_detail_skeleton)
            .shimmer(true)
            .show()

        getVoucherDetail()

        getMessage()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    flashbar?.dismiss()
                    isEnabled = false

                    findNavController().navigateUp()
                }
            })
    }

    private fun getVoucherDetail() {
        viewModel.getStoreDetail(voucherID).observe(this, Observer {
            it.handle(
                this, activity, null, skeletonScreen, getSkeletonTop()
            ) { data, code ->

                dataLayoutVisibility()

                if (data.useVocherConditions.isNullOrEmpty())
                    condition_layout.visibility = View.GONE
                else
                    condition_layout.visibility = View.VISIBLE

                if (data.voucherSlogans.isNullOrEmpty())
                    tick_text_list.visibility = View.GONE
                else
                    tick_text_list.visibility = View.VISIBLE

                title_text.text = data.title
                subtitle_text.text = data.subtitle
                getManexCountControl().setManexCount(data.manexCount!!)

                if (!data.useVocherConditions.isNullOrEmpty())
                    voucher_condition.setTextList(data.useVocherConditions!!)
                if (!data.voucherSlogans.isNullOrEmpty())
                    tick_text_list.setTextList(data.voucherSlogans!!)

                var text: String

                if (data.userCanBuy) {
                    text = getString(
                        R.string.burnvoucherdetail_buy_button_format,
                        data.manexCount.toString().toPersianNumber()
                    )
                } else {
                    text = getString(
                        R.string.burnvoucherdetail_button_format,
                        data.manexCountNeed.toString().toPersianNumber()
                    )
                }


                if (DeviceStatus.hasSoftKeys(requireActivity().windowManager, requireContext()))
                    height = DeviceStatus.getNavBarHeight(requireContext()).toFloat()


                setButtonSingle(text, data.userCanBuy) {

                    viewModel.getVoucherDialog(data.id).observe(this, Observer {
                        it.handle(this,requireActivity()){res,msg ->
                            var bottomSheet = PurchaseBottomSheetDialog.newInstance(
                                res.title,res.body
                            )
                            bottomSheet.show(childFragmentManager, "")
                            bottomSheet.setCallBack(object :
                                PurchaseBottomSheetDialog.BottomSheetCallBack {
                                override fun confirmCallBack() {
                                    //confirm click
                                    //api call
                                    buyVoucher(data.id)
                                }
                            })

                        }
                    })
                }

                //set collapse data
                hideManexCoinOfDescryption()
                setBackgroundColor(data.backgroundColor!!)
                setCollapsingTextColor(data.isBlackTheme)
                getManexCountControl().setEntry(
                    RequestType.Burn, CountLabelType.Collapse,
                    data.isBlackTheme
                )
                GlideApp.with(context!!).load(data.imageUrl).into(getIconImageView())
                getDescriptionTextView().text = data.expier
                var stringBuilder = StringBuilder()
                getTitleTextView().text =
                    stringBuilder.append("کارت هدیه").append(" ").append(data.name)

                getToolbar().setTitle(data.name.toString())
                getToolbar().showUpIcon(true) {
                    flashbar?.dismiss()
                    findNavController().navigateUp()
                }

                if (data.statusColor != null)
                    setBaseStatusColor(data.statusColor!!)
                else
                    setBaseStatusColor(data.backgroundColor!!)

                likeStatus = data.likeStatus
                setLikeVisibility(data.likeStatus)
            }
        })
    }

    fun buyVoucher(id: Long) {

        viewModel.buyVoucher(id).observe(requireActivity(), Observer { res ->
            res.handle(
                this@BurnVoucherDetailFragment,
                requireActivity(), null,
                null, null,
                null, null, { msg, code ->
                    viewModel.setErrorKey(msg.toString())
                }
            ) { it, code ->

                if (code == 200) {

                    findNavController().navigate(
                        BurnVoucherDetailFragmentDirections.purchase(it.value)
                    )

//                    //call purchase fragment and pass string data to it
//                    flashbar = SnackHelper.showSnack(
//                        requireActivity(),
//                        "خرید با موفقیت انجام شد . کالا به آدرس ثبت شده در پروفایل ارسال می گردد",
//                        height
//                    )
                } else {
                    viewModel.setErrorKey(code.toString())
                }
            }
        })
    }

    override fun onLikeIconClicked(v: View) {
        likeStatus = !likeStatus
        setLikeVisibility(likeStatus, true)

        viewModel.setVoucherLike(voucherID).observe(this, Observer {
            it.handle(this, requireActivity()) { data, code ->
                data
            }
        })
    }


    fun getMessage() {
        viewModel.errorMessage.observe(this, Observer {

            if (!it.isNullOrEmpty()) {
                it.forEach {
                    if (it.key == viewModel.errorKey.value) {
                        flashbar = SnackHelper.showSnack(
                            requireActivity(),
                            it.value,
                            height
                        )
                    }
                }
            } else {
                flashbar = SnackHelper.showSnack(
                    requireActivity(),
                    "خطایی رخ داده",
                    height
                )
            }

        })
    }


    override fun onShareIconClicked(v: View) {
        PublicFunction.shareLinkDialog(requireContext(), "www.google.com")
    }

    private fun getManexCount() {
        viewModel.refresh()
        viewModel.walletData.observe(this, Observer { res ->
            res.handle(this, activity) { it, code ->
                dataLayoutVisibility()
                getManexCountText().text = it.manexAmount.toInt().toString().toPersianNumber()
            }
        })
    }

    override fun showNoConnection() {
        contentLayoutBinding.mainLayout.visibility = View.GONE
        showNoInternetLayout()
    }

    private fun dataLayoutVisibility() {
        contentLayoutBinding.mainLayout.visibility = View.VISIBLE
        hideNoInternetLayout()
    }

    override fun onRetryClicked() {
        getManexCount()
        getVoucherDetail()
    }

}