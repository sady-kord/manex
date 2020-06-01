package com.baman.manex.ui.earn.earnLocalStore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.databinding.AdapterAddCardItemBinding

class EarnLocalStoreAddCardViewHolder
private constructor(val binding: AdapterAddCardItemBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun getInstance(parent: ViewGroup): EarnLocalStoreAddCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: AdapterAddCardItemBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.adapter_add_card_item, parent, false)
            return EarnLocalStoreAddCardViewHolder(
                binding
            )
        }
    }

    fun bind() {

    }
}