package com.baman.manex.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundViewHolder
import com.baman.manex.data.dto.ProfileMenuDto
import com.baman.manex.data.model.ProfileMenuType
import com.baman.manex.databinding.AdapterProfileBinding
import com.baman.manex.ui.common.UnSupportedViewTypeException
import com.baman.manex.util.glide.GlideApp
import timber.log.Timber

class ProfileAdapter(private val itemClickCallback: ((ProfileMenuDto?) -> Unit)?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_DIVIDER = 0
        const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DIVIDER -> ProfileDividerViewHolder.getInstance(
                parent
            )
            TYPE_ITEM -> {
                val binding: AdapterProfileBinding = DataBindingUtil.inflate<Nothing>(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_profile, parent, false
                )
                binding.text.setOnClickListener {
                    binding.item.let {
                        itemClickCallback?.invoke(it)
                    }
                }
                binding.image.setOnClickListener {
                    binding.item.let {
                        itemClickCallback?.invoke(it)
                    }
                }
                ProfileViewHolder(
                    binding
                )
            }
            else -> throw UnSupportedViewTypeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileDividerViewHolder -> holder.bind()
            else -> {
                (holder as ProfileViewHolder).bind(data[position] as ProfileMenuDto)
            }
        }
    }

    override fun getItemCount(): Int {
        val count = if (::data.isInitialized) data.size else 0
        Timber.d("getItemCount: $count")
        return count
    }

    override fun getItemViewType(position: Int): Int {
        val type = if (data[position] is String) TYPE_DIVIDER
        else TYPE_ITEM
        Timber.d("getItemViewType: $type")
        return type
    }

    private lateinit var data: List<Any>

    fun setData(list: List<ProfileMenuDto>) {
        val l = mutableListOf<Any>()

        list.forEach {
            when (ProfileMenuType.Parse(it.menuItemType)) {
                ProfileMenuType.DivisionItem -> {
                    l.add("divider")
                }
                ProfileMenuType.MenuItem -> {
                    l.add(it)
                }
            }
        }

        data = l
        notifyDataSetChanged()
    }

    class ProfileViewHolder(val adapterBinding: AdapterProfileBinding) :
        DataBoundViewHolder<AdapterProfileBinding>(adapterBinding) {

        fun bind(item: ProfileMenuDto) {

            adapterBinding.item = item

            GlideApp.with(adapterBinding.root.context).load(item.imagePath)
                .into(adapterBinding.image)

        }

    }
}