package com.baman.manex.ui.burn.burnManexStore.search

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
import com.baman.manex.controls.CountLabelType
import com.baman.manex.data.dto.ManexStoreInsideDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.databinding.AdapterManexStoreSearchItemBinding
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class BurnManexStoreSearchAdapter (
    appExecutors: AppExecutors,
    private val itemClickCallback: ((ManexStoreInsideDto?) -> Unit)?
) : DataBoundListAdapter<ManexStoreInsideDto, AdapterManexStoreSearchItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<ManexStoreInsideDto>() {
        override fun areItemsTheSame(
            oldItem: ManexStoreInsideDto,
            newItem: ManexStoreInsideDto
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ManexStoreInsideDto,
            newItem: ManexStoreInsideDto
        ): Boolean {
            return oldItem.manexCount == newItem.manexCount
                    && oldItem.progressValue == newItem.progressValue
        }
    }
) {

    override fun createBinding(parent: ViewGroup): AdapterManexStoreSearchItemBinding {
        val binding = DataBindingUtil.inflate<AdapterManexStoreSearchItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_manex_store_search_item,
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

    override fun bind(binding: AdapterManexStoreSearchItemBinding, item: ManexStoreInsideDto) {
        binding.item = item
        binding.manexstoreTextProductname.text = item.name
        binding.manexstoreTextProductmodel.text = item.model

        var colorSpan : ForegroundColorSpan

        if(item.progressValue == 100){
            binding.progressBar.progressDrawable = PublicFunction.getDrawable(R.drawable.background_progress_complete,binding.root.context)
            colorSpan = ForegroundColorSpan(ContextCompat.getColor(binding.root.context, R.color.adaptertem_manexcount_textcolor_completed))
        }else{
            binding.progressBar.progressDrawable = PublicFunction.getDrawable(R.drawable.background_progress,binding.root.context)
            colorSpan = ForegroundColorSpan(ContextCompat.getColor(binding.root.context, R.color.adaptertem_manexcount_textcolor))
        }

        binding.manexCountControlShopSearch.setManexCount(item.manexCount)
        binding.manexCountControlShopSearch.setEntry(RequestType.Burn , CountLabelType.ListItem)

        GlideApp.with(binding.root.context).load(item.imageUrl).into(binding.manexstoreImageProduct)
        binding.progressBar.progress = item.progressValue


    }


}