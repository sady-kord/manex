package com.baman.manex.ui.home.search

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.databinding.AdapterOnlineItemOldBinding
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class HomeSearchOnlineAdapter (
    appExecutors: AppExecutors,
    val isEarn : Boolean,
    private val itemClickCallback: ((OnlineStoreInsideDto) -> Unit)?
) : DataBoundListAdapter<OnlineStoreInsideDto, AdapterOnlineItemOldBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<OnlineStoreInsideDto>() {
        override fun areItemsTheSame(oldItem: OnlineStoreInsideDto, newItem: OnlineStoreInsideDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OnlineStoreInsideDto, newItem: OnlineStoreInsideDto): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.storeType == newItem.storeType
        }
    }
) {

    override fun createBinding(parent: ViewGroup): AdapterOnlineItemOldBinding {
        val binding = DataBindingUtil.inflate<AdapterOnlineItemOldBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_online_item_old,
            parent,
            false
        )

        binding.root.setOnClickListener {
            binding.item?.let {
                itemClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: AdapterOnlineItemOldBinding, item: OnlineStoreInsideDto) {
        binding.item = item

        if (isEarn) {

            binding.burnLayout.visibility = View.GONE
            binding.earnLayout.visibility = View.VISIBLE

            val textAmountBuilder = StringBuilder()
            textAmountBuilder.append(item.earnManexConditionTitle!!.title)
            binding.earnTextTitle.text = textAmountBuilder.toString().toPersianNumber()

            val manexCount = item.earnManexConditionTitle!!.manexCount.toString().toPersianNumber()
            val text = binding.root.context.getString(
                R.string.onlineadapteritem_subtitle_format,
                manexCount
            )
            val ssb = SpannableStringBuilder(text)
            val startingIndex = text.indexOf(manexCount)
            val colorSpan = ForegroundColorSpan(
                ContextCompat.getColor(
                    binding.root.context,
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
            val text = binding.root.context.getString(
                R.string.onlineadapteritem_burn_subtitle_format,
                manexCount
            )
            val ssb = SpannableStringBuilder(text)
            val startingIndex = text.indexOf(manexCount)
            val colorSpan = ForegroundColorSpan(
                ContextCompat.getColor(
                    binding.root.context,
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


        GlideApp.with(binding.root.context).load(item.imagePath).into(binding.imageIcon)
    }


}