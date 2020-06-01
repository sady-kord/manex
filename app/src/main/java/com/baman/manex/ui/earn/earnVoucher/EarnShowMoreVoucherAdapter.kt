package com.baman.manex.ui.earn.earnVoucher

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.controls.CountLabelType
import com.baman.manex.data.dto.VoucherInsideDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.databinding.AdapterEarnVoucherItemBinding
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class EarnShowMoreVoucherAdapter (
    appExecutors: AppExecutors,
    private val itemClickCallback: ((VoucherInsideDto?) -> Unit)?
) : DataBoundListAdapter<VoucherInsideDto, AdapterEarnVoucherItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<VoucherInsideDto>() {
        override fun areItemsTheSame(oldItem: VoucherInsideDto, newItem: VoucherInsideDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: VoucherInsideDto, newItem: VoucherInsideDto): Boolean {
            return oldItem.manexCount == newItem.manexCount
                    && oldItem.progressValue == newItem.progressValue
        }
    }
) {

    override fun createBinding(parent: ViewGroup): AdapterEarnVoucherItemBinding {
        val binding = DataBindingUtil.inflate<AdapterEarnVoucherItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_earn_voucher_item,
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

    override fun bind(binding: AdapterEarnVoucherItemBinding, item: VoucherInsideDto) {
        binding.item = item

        binding.voucherTitle.text = item.title.toPersianNumber()
        binding.voucherSubtitle.text = item.subTitle

        binding.manexCountControl.setManexCount(item.manexCount)
        binding.manexCountControl.setEntry(RequestType.Earn, CountLabelType.ListItem)

        GlideApp.with(binding.root.context).load(item.imageUrl).into(binding.voucherImageView)

        val amount = item.price.toString().toPersianNumber()
        val textAmount = binding.root.context.getString(R.string.amount_format, amount)
        val amountSpan = SpannableStringBuilder(textAmount)
        val startingIndexAmount = textAmount.indexOf(amount)

        val colorSpanAmount = ForegroundColorSpan(
            ContextCompat.getColor(
                binding.root.context,
                R.color.colorTextPrimary
            )
        )

        amountSpan.setSpan(
            colorSpanAmount,
            startingIndexAmount,
            startingIndexAmount + textAmount.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        var stringBuilder = StringBuilder()
        stringBuilder.append(amount).append(" ").append("تومان")

        binding.voucherAmount.text = PublicFunction.increaseFontSizeForPath(amountSpan, amount, 1.25f)


    }

}