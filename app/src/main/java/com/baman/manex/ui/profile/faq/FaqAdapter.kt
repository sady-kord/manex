package com.baman.manex.ui.profile.faq

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.data.dto.FaqDto
import com.baman.manex.databinding.AdapterFaqBinding
import com.baman.manex.databinding.AdapterInboxBinding
import com.baman.manex.ui.profile.inbox.InboxDto

class FaqAdapter  (
    appExecutors: AppExecutors,
    private val itemClickCallback: ((FaqDto?) -> Unit)?
) : DataBoundListAdapter<FaqDto, AdapterFaqBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<FaqDto>() {
        override fun areItemsTheSame(oldItem: FaqDto, newItem: FaqDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FaqDto, newItem: FaqDto): Boolean {
            return oldItem.title== newItem.title
                    && oldItem.id== newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): AdapterFaqBinding {
        val binding = DataBindingUtil.inflate<AdapterFaqBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_faq,
            parent,
            false
        )

        binding.root.setOnClickListener {
            expand(binding)
        }
        return binding
    }

    override fun bind(binding: AdapterFaqBinding, item: FaqDto) {

        binding.item = item

        var list = mutableListOf<CharSequence>()
        list.add(item.description)

        binding.numberedText.setTextList(list)

    }

    private fun expand(binding: AdapterFaqBinding) {
        val expanded = binding.item?.collapse ?: return
        val duration = 400L
        val detail = binding.numberedText
        val arrow = binding.arrowImage
        detail.pivotY = 0f
        if (expanded) {
            detail.animate()
                .scaleY(0f)
                .setDuration(duration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        detail.visibility = View.GONE
                    }
                })
                .start()
            arrow.animate()
                .rotation(270f)
                .setDuration(duration / 2)
                .start()
        } else {
            detail.visibility = View.VISIBLE
            detail.scaleY = 0f
            detail.animate()
                .scaleY(1f)
                .setDuration(duration)
                .setListener(null)
                .start()
            arrow.animate()
                .rotation(90f)
                .setDuration(duration / 2)
                .start()
        }
        binding.item?.collapse = !expanded
    }


}