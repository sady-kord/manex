package com.baman.manex.ui.home

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.data.dto.ManexStoreInsideDto
import com.baman.manex.databinding.AdapterManexStoreOfferItemBinding
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class ManexStoreOfferAdapter (
    appExecutors: AppExecutors,
    private val itemClickCallback: ((ManexStoreInsideDto?) -> Unit)?
) : DataBoundListAdapter<ManexStoreInsideDto, AdapterManexStoreOfferItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<ManexStoreInsideDto>() {
        override fun areItemsTheSame(oldItem: ManexStoreInsideDto, newItem: ManexStoreInsideDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ManexStoreInsideDto, newItem: ManexStoreInsideDto): Boolean {
            return oldItem.manexCount == newItem.manexCount
                    && oldItem.progressValue == newItem.progressValue
        }
    }
) {

    override fun createBinding(parent: ViewGroup): AdapterManexStoreOfferItemBinding {
        val binding = DataBindingUtil.inflate<AdapterManexStoreOfferItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_manex_store_offer_item,
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

    override fun bind(binding: AdapterManexStoreOfferItemBinding, item: ManexStoreInsideDto) {

        binding.manexstoreTextProductname.text = item.name
        binding.manexstoreTextProductmodel.text = item.model

        val manexCountText = item.manexCount.toString().toPersianNumber()
        val text = binding.root.context.getString(R.string.manexstoreadapteritem_subtitle_format, manexCountText)
        val ssb = SpannableStringBuilder(text)
        val startingIndex = text.indexOf(manexCountText)
        var colorSpan : ForegroundColorSpan
        if(item.progressValue == 100){
            binding.progressBar.progressDrawable = PublicFunction.getDrawable(R.drawable.background_progress_complete,binding.root.context)
            colorSpan = ForegroundColorSpan(ContextCompat.getColor(binding.root.context, R.color.adaptertem_manexcount_textcolor_completed))
        }else{
            binding.progressBar.progressDrawable = PublicFunction.getDrawable(R.drawable.background_progress,binding.root.context)
            colorSpan = ForegroundColorSpan(ContextCompat.getColor(binding.root.context, R.color.adaptertem_manexcount_textcolor))
        }

        ssb.setSpan(colorSpan, startingIndex, startingIndex + manexCountText.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)


        val typeface = ResourcesCompat.getFont(binding.root.context, R.font.iranyekan_bold)

        ssb.setSpan(
            CustomTypefaceSpan("", typeface),
            startingIndex, startingIndex + manexCountText.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )



        binding.manexstoreTextManexcount.text = PublicFunction.increaseFontSizeForPath(ssb, manexCountText, 1.25f)

        GlideApp.with(binding.root.context).load(item.imageUrl).into(binding.manexstoreImageProduct)
        binding.progressBar.progress = item.progressValue

    }


}