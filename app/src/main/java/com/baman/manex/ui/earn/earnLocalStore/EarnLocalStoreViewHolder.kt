package com.baman.manex.ui.earn.earnLocalStore

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.controls.CountLabelType
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.data.dto.StoreInfoDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.databinding.AdapterLocalStoreBinding
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class EarnLocalStoreViewHolder
private constructor(val binding: AdapterLocalStoreBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun getInstance(
            parent: ViewGroup,
            itemClickCallback: ((StoreInfoDto?) -> Unit)?
        ): EarnLocalStoreViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: AdapterLocalStoreBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.adapter_local_store, parent, false)

            binding.root.setOnClickListener {
                itemClickCallback!!.invoke(binding.item)
            }

            return EarnLocalStoreViewHolder(
                binding
            )
        }
    }

    fun bind(item: StoreInfoDto) {

        binding.item = item

        val amountValue = item.earnManexConditionTitle!!.title.toPersianNumber()
        binding.localstoreAmountText.text = amountValue

        binding.manexCountControl.setManexCount(item.earnManexConditionTitle!!.manexCount)
        binding.manexCountControl.setEntry(RequestType.Earn , CountLabelType.ListItem)

        binding.localstoreNameText.text = item.name
        binding.localstoreAddressText.text = item.address
        GlideApp.with(binding.root.context).load(item.imagePath).into(binding.iconImageView)


    }
}