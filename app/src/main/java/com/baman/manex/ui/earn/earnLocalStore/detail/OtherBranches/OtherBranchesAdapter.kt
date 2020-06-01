package com.baman.manex.ui.earn.earnLocalStore.detail.OtherBranches

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.data.dto.OtherBranchDto
import com.baman.manex.databinding.ViewBranchItemBinding
import com.baman.manex.util.PublicFunction
import kotlinx.android.synthetic.main.view_branch_item.view.*

class OtherBranchesAdapter(
    appExecutors: AppExecutors
) : DataBoundListAdapter<OtherBranchDto, ViewBranchItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<OtherBranchDto>() {
        override fun areItemsTheSame(oldItem: OtherBranchDto, newItem: OtherBranchDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OtherBranchDto, newItem: OtherBranchDto): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.address == newItem.address
        }
    }
){

    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

    override fun createBinding(parent: ViewGroup): ViewBranchItemBinding {
        val binding = DataBindingUtil.inflate<ViewBranchItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.view_branch_item ,
            parent,
            false
        )

        return binding
    }

    override fun bind(binding: ViewBranchItemBinding, item: OtherBranchDto) {
        binding.item = item

        binding.branchCallImage.setOnClickListener {
            binding.root.context.startActivity(
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts("tel", "02188985421", null)
                )
            )
        }

        binding.branchDirectionImage.setOnClickListener {
            PublicFunction.shareLocationDialog(binding.root.context,item.latitude,item.longitude)
        }

    }
}