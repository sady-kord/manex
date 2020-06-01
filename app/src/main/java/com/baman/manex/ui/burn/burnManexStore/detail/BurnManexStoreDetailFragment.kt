package com.baman.manex.ui.burn.burnManexStore.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andrognito.flashbar.Flashbar
import com.baman.manex.R
import com.baman.manex.controls.CountLabelType
import com.baman.manex.controls.buttonlistsheet.PurchaseBottomSheetDialog
import com.baman.manex.data.dto.ManexStoreInsideDto
import com.baman.manex.data.dto.StoreManexInfoDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.databinding.FragmentBurnstoremanexdetailBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.RetryCallBack
import com.baman.manex.ui.baseClass.ServiceDetailFragment
import com.baman.manex.util.*
import com.baman.manex.util.snack.SnackHelper
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import javax.inject.Inject

class BurnManexStoreDetailFragment : ServiceDetailFragment(), Injectable,
    InternetConnection, RetryCallBack {

    private var shopID: Long = 0
    private lateinit var storeManex: ManexStoreInsideDto
    private var containerLayoutBinding by autoCleared<FragmentBurnstoremanexdetailBinding>()
    private lateinit var viewModel: BurnManexStoreDetailViewModel
    private var hasAddress: Boolean = false
    private var likeStatus = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val args: BurnManexStoreDetailFragmentArgs by navArgs()
    private var flashbar: Flashbar? = null
    private lateinit var skeletonScreen: ViewSkeletonScreen

    var height = 86f

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        containerLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_burnstoremanexdetail, container, false
        )
        return containerLayoutBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(BurnManexStoreDetailViewModel::class.java)

        setRetryCallBack(this)

        storeManex = args.store

        args.store.id?.let { shopID = it }

        skeletonScreen = Skeleton.bind(containerLayoutBinding.root)
            .load(R.layout.fragment_burnstoremanexdetail_skeleton)
            .shimmer(true)
            .show()


        if (DeviceStatus.hasSoftKeys(requireActivity().windowManager, requireContext()))
            height = DeviceStatus.getNavBarHeight(requireContext()).toFloat()




        getStoreDetail()

        getManexCount()

        getMessage()
    }

    private fun getStoreDetail() {
        viewModel.getStoreDetail(storeManex.id).observe(this, Observer { it ->
            it.handle(
                this, activity, null, skeletonScreen
                , getSkeletonTop()
            ) { it, code ->
                dataLayoutVisibility()
                bindUi(it)
            }
        })
    }

    private fun bindUi(storeManexInfoDto: StoreManexInfoDto) {

        containerLayoutBinding.storeInfo = storeManexInfoDto

        containerLayoutBinding.specControl.setDetailsData(storeManexInfoDto.productDetails)

        containerLayoutBinding.manexCountControlShopDetail.setManexCount(storeManexInfoDto.manexCount)
        containerLayoutBinding.manexCountControlShopDetail.setEntry(
            RequestType.Burn,
            CountLabelType.ListItem
        )

        setImageSlider(storeManexInfoDto.imageUrls)

        var text: String

        if (storeManexInfoDto.userCanBuy) {
            text = getString(
                R.string.burnstoremanexdetail_buy_button_format,
                storeManexInfoDto.manexCount.toString().toPersianNumber()
            )
        } else {
            text = getString(
                R.string.burnstoremanexdetail_button_format,
                storeManexInfoDto.manexCountNeed.toString().toPersianNumber()
            )
        }


        viewModel.getHasAddress().observe(this, Observer {
            it.handle(this, requireActivity()) { it, code ->
                hasAddress = it.hasAddress
            }
        })

        setButtonSingle(text, storeManexInfoDto.userCanBuy) {


            viewModel.getStoreConfirmDialog(storeManexInfoDto.id).observe(this, Observer {
                it.handle(this, requireActivity()) { it, msg ->

                    var bottomSheet = PurchaseBottomSheetDialog.newInstance(
                        it.title,
                        it.body
                    )
                    bottomSheet.show(childFragmentManager, "")
                    bottomSheet.setCallBack(object :
                        PurchaseBottomSheetDialog.BottomSheetCallBack {
                        override fun confirmCallBack() {
                            buyShop(storeManexInfoDto.id)
                        }
                    })

                }
            })

        }

        likeStatus = storeManexInfoDto.likeStatus
        setLikeVisibility(storeManexInfoDto.likeStatus)

        setBackgroundColor("ffffff")

        if (!storeManexInfoDto.shortName.isNullOrEmpty())
            getToolbar().setTitle(storeManexInfoDto.shortName)

        setImageUpColor("000000")

        getToolbar().showUpIcon(true) {
            flashbar?.dismiss()
            findNavController().navigateUp()
        }

    }

    fun buyShop(id: Long) {
        viewModel.buyGood(id).observe(requireActivity(), Observer { res ->
            res.handle(this@BurnManexStoreDetailFragment, requireActivity(),
                null, null, null,
                null, null, { error, code ->
                    viewModel.setErrorKey(error)
                }) { it, code ->
                var a = it.hasAddress

                getManexCount()

                if (a) {
                    flashbar = SnackHelper.showSnack(
                        requireActivity(),
                        "خرید با موفقیت انجام شد . کالا به آدرس ثبت شده در پروفایل ارسال می گردد",
                        height
                    )
                } else {
                    flashbar = SnackHelper.showSnack(
                        requireActivity(),
                        "خرید با موفقیت انجام شد . لطفا آدرس خود را در پرفایل ثبت کنید",
                        height
                    )
                }

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


    override fun onLikeIconClicked(v: View) {
        likeStatus = !likeStatus

        setLikeVisibility(likeStatus, true)

        viewModel.setShopLike(shopID).observe(this, Observer {
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
        getManexCount()
        getStoreDetail()
    }
}