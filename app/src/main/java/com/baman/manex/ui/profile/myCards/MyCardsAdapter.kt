package com.baman.manex.ui.profile.myCards

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.data.dto.RegisterCardDto
import com.baman.manex.databinding.AdapterMyCardsBinding
import com.baman.manex.util.glide.GlideApp

class MyCardsAdapter(
    appExecutors: AppExecutors
) : DataBoundListAdapter<RegisterCardDto, AdapterMyCardsBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<RegisterCardDto>() {
        override fun areItemsTheSame(oldItem: RegisterCardDto, newItem: RegisterCardDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RegisterCardDto,
            newItem: RegisterCardDto
        ): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.title == newItem.title
        }
    }
) {

    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

    override fun createBinding(parent: ViewGroup): AdapterMyCardsBinding {
        val binding = DataBindingUtil.inflate<AdapterMyCardsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_my_cards ,
            parent,
            false
        )

        return binding
    }

    override fun bind(binding: AdapterMyCardsBinding, item: RegisterCardDto) {
        binding.item = item

        var part1 = item.cardPin.substring(0 , 4)
        var part2 = item.cardPin.substring(4 , 6)
        var part3 = item.cardPin.substring(12 , 16)

        var stingBuilder = StringBuilder()
        stingBuilder.append(part1).append(" - ").append(part2).append("—")
            .append(" - —— - ").append(part3)

        binding.cardNumber.text = stingBuilder

        GlideApp.with(binding.root.context).load(item.imageUrl).into(binding.bankImage)

    }
}

