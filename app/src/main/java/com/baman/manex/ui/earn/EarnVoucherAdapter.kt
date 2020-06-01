package com.baman.manex.ui.earn

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.baman.manex.R
import com.baman.manex.controls.CountLabelType
import com.baman.manex.controls.ManexCountLabelControl
import com.baman.manex.data.dto.VoucherInsideDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class EarnVoucherAdapter(
    val context: Context,
    val listener: (VoucherInsideDto) -> Unit
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var data: List<VoucherInsideDto>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        VoucherViewHolder.newInstance(
            parent,
            listener
        )

    override fun onCreateLayoutHelper(): LinearLayoutHelper {
        val helper = LinearLayoutHelper()
        val hPadding =
            context.resources.getDimensionPixelSize(R.dimen.main_recycler_horizontalpadding)
        helper.setPadding(hPadding, 0, hPadding, 0)
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VoucherViewHolder).bind(data[position])
    }

    override fun getItemCount() = if (::data.isInitialized) data.size else 0

    fun setData(items: List<VoucherInsideDto>) {
        data = items
        notifyDataSetChanged()
    }
}

class VoucherViewHolder(
    itemView: View,
    val listener: (VoucherInsideDto) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val text_name: TextView = itemView.findViewById(R.id.voucher_title)
    private val text_subtitle: TextView = itemView.findViewById(R.id.voucher_subtitle)
    private val manex_count: ManexCountLabelControl =
        itemView.findViewById(R.id.manex_count_control)
    private val text_amount: TextView = itemView.findViewById(R.id.voucher_amount)
    private val item_image: ImageView = itemView.findViewById(R.id.voucher_image_view)

    fun bind(item: VoucherInsideDto) {
        text_name.text = item.title.toPersianNumber()
        text_subtitle.text = item.subTitle
        manex_count.setManexCount(item.manexCount)
        manex_count.setEntry(RequestType.Earn, CountLabelType.ListItem)

        GlideApp.with(itemView.context).load(item.imageUrl).into(item_image)

        var a = PublicFunction.addPriceSeparator(item.price)

        val amount = a.toPersianNumber()
        val textAmount = itemView.context.getString(R.string.amount_format, amount)
        val amountSpan = SpannableStringBuilder(textAmount)
        val startingIndexAmount = textAmount.indexOf(amount)

        val colorSpanAmount = ForegroundColorSpan(
            ContextCompat.getColor(
                itemView.context,
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


        var b = PublicFunction.increaseFontSizeForPath(amountSpan, amount, 1.25f)


        text_amount.text = b

        itemView.setOnClickListener {
            listener.invoke(item)
        }
    }

    companion object {
        fun newInstance(
            parent: ViewGroup,
            listener: (VoucherInsideDto) -> Unit
        ): VoucherViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(
                R.layout.adapter_earn_voucher_item,
                parent,
                false
            )

            return VoucherViewHolder(itemView, listener)
        }
    }
}

