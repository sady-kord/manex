package com.baman.manex.ui.burn.burnVoucher.purchase

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
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
import com.baman.manex.data.dto.VoucherDetailDto
import com.baman.manex.databinding.FragmentPurchaseVoucherDetailBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.util.DeviceStatus
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.autoCleared
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.snack.SnackHelper
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import javax.inject.Inject


class PurchaseVoucherDetailFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentPurchaseVoucherDetailBinding>()
    private lateinit var viewModel: PurchaseVoucherDetailViewModel

    val args: PurchaseVoucherDetailFragmentArgs by navArgs()
//    private lateinit var skeletonScreen: ViewSkeletonScreen

    private var flashbar: Flashbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_purchase_voucher_detail, container, false
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(PurchaseVoucherDetailViewModel::class.java)

//        skeletonScreen = Skeleton.bind(binding.root)
//            .load(R.layout.fragment_purchase_voucher_detail_skeleton)
//            .shimmer(true)
//            .show()

        viewModel.purchaseDetail(args.storeId).observe(this, Observer {
            it.handle(this, requireActivity() ) { it, code ->

                setData(it)
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    SnackHelper?.dismiss()
                    isEnabled = false

                    findNavController().navigateUp()
                }
            })
    }

    fun setData(voucherDetail: VoucherDetailDto) {

        binding.mainLayoutPurchase.setBackgroundColor(parseColor(voucherDetail.backgroundColor!!))
        binding.toolbar.setBackgroundColor(parseColor(voucherDetail.backgroundColor!!))

        if (voucherDetail.isBlackTheme) {
            binding.shopName.setTextColor(binding.root.context.resources.getColor(R.color.colorTextPrimary))
            binding.shopSubtitle.setTextColor(binding.root.context.resources.getColor(R.color.colorTextPrimary))

            binding.backIamgeToolbar.setImageDrawable(binding.root.context.resources.getDrawable(R.drawable.ic_back_black))
            binding.shareImageToolbar.setImageDrawable(binding.root.context.resources.getDrawable(R.drawable.ic_share_black))

            //set color share and back
        } else {
            binding.shopName.setTextColor(binding.root.context.resources.getColor(R.color.white))
            binding.shopSubtitle.setTextColor(binding.root.context.resources.getColor(R.color.white))

            //set color share and back
            binding.backIamgeToolbar.setImageDrawable(binding.root.context.resources.getDrawable(R.drawable.ic_back_white))
            binding.shareImageToolbar.setImageDrawable(binding.root.context.resources.getDrawable(R.drawable.ic_share))
        }

        binding.backIamgeToolbar.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.shareImageToolbar.setOnClickListener {
            PublicFunction.shareLinkDialog(requireContext(), voucherDetail.shareLink!!)
        }

        binding.textList.setTextColor(binding.root.context.resources.getColor(R.color.colorTextPrimary))
        binding.shopName.text = voucherDetail.shortName
        binding.shopSubtitle.text = voucherDetail.name

        //check link null
        //todo
        binding.shopButton.text = voucherDetail.linkText
        binding.shopButton.setOnClickListener {
            PublicFunction.openUrlInBrowser(context!! , voucherDetail.link!!)
        }

        GlideApp.with(context!!).load(voucherDetail.imageUrl).into(binding.logoPurchase)

        setBaseStatusColor(voucherDetail.statusColor!!)

        if (voucherDetail.useVocherConditions != null)
            binding.textList.setTextList(voucherDetail.useVocherConditions)

        binding.voucherCode.text = voucherDetail.voucherCode

        binding.copyButton.setOnClickListener {
            val clipboard = context!!.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("DISCOUNT_CODE", voucherDetail.voucherCode)
            clipboard!!.setPrimaryClip(clip)
            var height = 60f
            if (DeviceStatus.hasSoftKeys(requireActivity().windowManager, requireContext()))
                height = DeviceStatus.getNavBarHeight(requireContext()).toFloat()
            SnackHelper.showSnack(requireActivity(), getString(R.string.copied), height)
        }
    }
}
