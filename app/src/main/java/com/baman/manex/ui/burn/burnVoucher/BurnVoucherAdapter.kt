package com.baman.manex.ui.burn.burnVoucher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.controls.CountLabelType
import com.baman.manex.data.dto.VoucherInsideDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.databinding.AdapterVoucherCodeItemBinding
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class BurnVoucherAdapter(
    appExecutors: AppExecutors,
    private val itemClickCallback: ((VoucherInsideDto?) -> Unit)?
) : DataBoundListAdapter<VoucherInsideDto, AdapterVoucherCodeItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<VoucherInsideDto>() {
        override fun areItemsTheSame(
            oldItem: VoucherInsideDto,
            newItem: VoucherInsideDto
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: VoucherInsideDto,
            newItem: VoucherInsideDto
        ): Boolean {
            return oldItem.manexCount == newItem.manexCount
                    && oldItem.progressValue == newItem.progressValue
        }
    }
) {

    override fun createBinding(parent: ViewGroup): AdapterVoucherCodeItemBinding {
        val binding = DataBindingUtil.inflate<AdapterVoucherCodeItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_voucher_code_item,
            parent,
            false
        )

        binding.root.setOnClickListener {
            binding.item.let {
                itemClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: AdapterVoucherCodeItemBinding, item: VoucherInsideDto) {
        binding.item = item

        if (item.title != null)
            binding.textName.text = item.title.toPersianNumber()

        if (item.subTitle != null)
            binding.textSubtitle.text = item.subTitle.toPersianNumber()

        binding.manexCountControlVoucher.setManexCount(item.manexCount)
        binding.manexCountControlVoucher.setEntry(RequestType.Burn, CountLabelType.ListItem)

        if (item.expiryText != null)
            binding.textExpireTime.text = item.expiryText.toPersianNumber()
        binding.progressBar.progress = item.progressValue

        GlideApp.with(binding.root.context).load(item.imageUrl).into(binding.itemImage)

    }

}