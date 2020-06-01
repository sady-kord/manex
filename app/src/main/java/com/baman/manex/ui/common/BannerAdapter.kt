package com.baman.manex.ui.common

import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.data.dto.BannerDetailDto
import com.baman.manex.data.dto.BannerDto
import com.baman.manex.databinding.AdapterBurnBannerBinding
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.glide.GlideModule
import com.baman.manex.util.glide.SvgDecoder
import com.baman.manex.util.glide.SvgDrawableTranscoder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.StreamEncoder
import com.caverock.androidsvg.SVG
import java.io.InputStream

class BannerAdapter(
    appExecutors: AppExecutors
) : DataBoundListAdapter<BannerDetailDto, AdapterBurnBannerBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<BannerDetailDto>() {
        override fun areItemsTheSame(oldItem: BannerDetailDto, newItem: BannerDetailDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BannerDetailDto,
            newItem: BannerDetailDto
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.imageUrl == newItem.imageUrl
        }

    }
) {

    override fun createBinding(parent: ViewGroup): AdapterBurnBannerBinding {
        val binding = DataBindingUtil.inflate<AdapterBurnBannerBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_burn_banner,
            parent,
            false
        )

        return binding
    }

    override fun bind(binding: AdapterBurnBannerBinding, item: BannerDetailDto) {
        GlideApp.with(binding.root.context)
            .load(item.imageUrl)
            .into(binding.bannerImage)
    }
}