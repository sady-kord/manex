package com.baman.manex.ui.earn

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.databinding.AdapterOnlineItemBinding
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.baman.manex.data.dto.StoreInfoDto
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.PublicFunction


class OnlineServiceViewHolder(
    val binding: AdapterOnlineItemBinding,
    private val isEarn: Boolean,
    listener: (StoreInfoDto) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun getInstance(
            parent: ViewGroup,
            isEarn: Boolean,
            listener: (StoreInfoDto) -> Unit
        ): OnlineServiceViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                DataBindingUtil.inflate<AdapterOnlineItemBinding>(
                    inflater,
                    R.layout.adapter_online_item,
                    parent,
                    false
                )

            return OnlineServiceViewHolder(
                binding,
                isEarn,
                listener
            )
        }
    }

    private var item: StoreInfoDto? = null

    init {
        binding.root.setOnClickListener { item?.let { listener.invoke(it) } }
    }

    fun bind(item: StoreInfoDto) {
        this.item = item

        if (isEarn) {

            binding.burnLayout.visibility = View.GONE
            binding.earnLayout.visibility = View.VISIBLE

            val textAmountBuilder = StringBuilder()
            textAmountBuilder.append(item.earnManexConditionTitle!!.title)
            binding.earnTextTitle.text = textAmountBuilder.toString().toPersianNumber()

            val manexCount = item.earnManexConditionTitle!!.manexCount.toString().toPersianNumber()
            val text = itemView.context.getString(
                R.string.onlineadapteritem_subtitle_format,
                manexCount
            )
            val ssb = SpannableStringBuilder(text)
            val startingIndex = text.indexOf(manexCount)
            val colorSpan = ForegroundColorSpan(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.adaptertem_manexcount_textcolor
                )
            )
            ssb.setSpan(
                colorSpan, startingIndex, startingIndex + manexCount.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )

            val typeface = ResourcesCompat.getFont(binding.root.context, R.font.iranyekan_bold)

            ssb.setSpan(
                CustomTypefaceSpan("", typeface),
                startingIndex, startingIndex + manexCount.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )

            binding.earnTextSubtitle.text = PublicFunction.increaseFontSizeForPath(ssb, manexCount, 1.25f)

        } else {

            binding.burnLayout.visibility = View.VISIBLE
            binding.earnLayout.visibility = View.GONE

            val textAmountBuilder = StringBuilder()
            textAmountBuilder.append(item.burnManexConditionTitle!!.title)
            binding.textTitle.text = textAmountBuilder.toString().toPersianNumber()

            val manexCount = item.burnManexConditionTitle!!.manexCount.toString().toPersianNumber()
            val text = itemView.context.getString(
                R.string.onlineadapteritem_burn_subtitle_format,
                manexCount
            )
            val ssb = SpannableStringBuilder(text)
            val startingIndex = text.indexOf(manexCount)
            val colorSpan = ForegroundColorSpan(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.adaptertem_manexcount_textcolor
                )
            )
            ssb.setSpan(
                colorSpan, startingIndex, startingIndex + manexCount.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )

            val typeface = ResourcesCompat.getFont(binding.root.context, R.font.iranyekan_bold)

            ssb.setSpan(
                CustomTypefaceSpan("", typeface),
                startingIndex, startingIndex + manexCount.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )

            binding.textSubtitle.text = PublicFunction.increaseFontSizeForPath(ssb, manexCount, 1.25f)


        }


        GlideApp.with(itemView.context).load(item.imagePath).into(binding.imageIcon)

    }
}