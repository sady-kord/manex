package com.baman.manex.ui.earn.earnLocalStore.detail.nearBranch

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
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.databinding.ViewOthernearbystoreBinding
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class NearBranchAdapter (
    appExecutors: AppExecutors,
    private val itemClickCallback: ((OnlineStoreInsideDto?) -> Unit)?
) : DataBoundListAdapter<OnlineStoreInsideDto, ViewOthernearbystoreBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<OnlineStoreInsideDto>() {
        override fun areItemsTheSame(oldItem: OnlineStoreInsideDto, newItem: OnlineStoreInsideDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OnlineStoreInsideDto, newItem: OnlineStoreInsideDto): Boolean {
            return oldItem.latitude == newItem.latitude
                    && oldItem.id== newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ViewOthernearbystoreBinding {
        val binding = DataBindingUtil.inflate<ViewOthernearbystoreBinding>(
            LayoutInflater.from(parent.context),
            R.layout.view_othernearbystore,
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

    override fun bind(binding: ViewOthernearbystoreBinding, item: OnlineStoreInsideDto) {

        binding.item = item

        val amountValue = item.earnManexConditionTitle!!.price.toString().toPersianNumber()
        binding.localstoreAmountText.text = "هر $amountValue هزار تومان"
        
        binding.manexCountControlNearBranch.setManexCount(item.earnManexConditionTitle!!.manexCount)
        binding.manexCountControlNearBranch.setEntry(RequestType.Earn , CountLabelType.ListItem)

        binding.localstoreNameText.text = item.name
        binding.localstoreAddressText.text = item.address
        GlideApp.with(binding.root.context).load(item.imagePath).into(binding.iconImageView)

    }


}