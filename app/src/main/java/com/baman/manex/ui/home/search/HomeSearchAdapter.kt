package com.baman.manex.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.data.dto.BurnDto
import com.baman.manex.data.dto.ManexStoreInsideDto
import com.baman.manex.data.dto.StoresDTO
import com.baman.manex.databinding.AdapterHomeSearchBinding

class HomeSearchAdapter(
    val appExecutors: AppExecutors,
    val itemClickCallback: ((Any,Boolean?,Boolean?) -> Unit)?
) : DataBoundListAdapter<Any, AdapterHomeSearchBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return false
        }
    }
) {

    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

    override fun createBinding(parent: ViewGroup): AdapterHomeSearchBinding {
        val binding = DataBindingUtil.inflate<AdapterHomeSearchBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_home_search,
            parent,
            false
        )

        return binding
    }

    override fun bind(binding: AdapterHomeSearchBinding, item: Any) {

        binding.list.layoutManager = LinearLayoutManager(binding.root.context)
        var adapter =
            HomeSearchSectionAdapter { it, isEarn, isOnline ->
                itemClickCallback?.invoke(it, isEarn, isOnline)
            }
        binding.list.adapter = adapter

        when (item) {
            is BurnDto -> {
                binding.header.text = "خرج منکس"
                adapter.setData(item, appExecutors)
            }
            is StoresDTO -> {
                if (!item.offline.branches.isNullOrEmpty() || !item.onLine.branches.isNullOrEmpty()) {
                    binding.header.text = "کسب منکس"
                    adapter.setData(item, appExecutors)
                }
            }
        }

    }
}