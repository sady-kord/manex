package com.baman.manex.ui.profile.inbox

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.databinding.AdapterInboxBinding
import com.baman.manex.databinding.AdapterMyTransactionBinding
import com.baman.manex.databinding.ViewOthernearbystoreBinding
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber


class InboxDto(val id: Int,val name: String,var isExpanded: Boolean = false)

class InboxAdapter  (
    appExecutors: AppExecutors,
    private val itemClickCallback: ((InboxDto?) -> Unit)?
) : DataBoundListAdapter<InboxDto, AdapterInboxBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<InboxDto>() {
        override fun areItemsTheSame(oldItem: InboxDto, newItem: InboxDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: InboxDto, newItem: InboxDto): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.id== newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): AdapterInboxBinding {
        val binding = DataBindingUtil.inflate<AdapterInboxBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_inbox,
            parent,
            false
        )

        binding.root.setOnClickListener {
            expand(binding)
        }
        return binding
    }

    override fun bind(binding: AdapterInboxBinding, item: InboxDto) {

        binding.item = item

        var list = mutableListOf<CharSequence>()
        list.add("شما با ۵ بار استفاده از تپسی در طول یک\u200Cماه ۱۰ منکس بیشتر دریافت خواهید کرد. ")

        binding.numberedText.setTextList(list)

    }

    private fun expand(binding: AdapterInboxBinding) {
        val expanded = binding.item?.isExpanded ?: return
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
        binding.item?.isExpanded = !expanded
    }


}