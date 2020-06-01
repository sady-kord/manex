package com.baman.manex.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.layout.LinearLayoutHelper

@Deprecated("Add show more when setting up tabbed list")
abstract class ShowMoreAdapter<VH: RecyclerView.ViewHolder> : DelegateAdapter.Adapter<VH>() {

    companion object {
        private const val ITEM_TYPE_EMPTY = 45434
        private const val ITEM_TYPE_SHOW_MORE = 9672
    }

    private var isOneColumn = true

    init {
        isOneColumn = onCreateLayoutHelper() is LinearLayoutHelper
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_TYPE_EMPTY ->
                EmptyViewHolder.newInstance(parent) as VH
            ITEM_TYPE_SHOW_MORE ->
                ShowMoreViewHolder.newInstance(
                    parent
                ) as VH
            else -> onCreateViewHolder2(parent, viewType)
        }
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        when (holder) {
            is EmptyViewHolder -> { }
            is ShowMoreViewHolder -> { }
            else -> onBindViewHolder2(holder, position)
        }
    }

    private fun isEven() = getItemCount2() % 2 == 0

    override fun getItemCount() = getItemCount2() + if (!isOneColumn && !isEven()) 2 else 1

    final override fun getItemViewType(position: Int): Int {
        return if (isOneColumn) {
            if (position == getItemCount2() - 1) ITEM_TYPE_SHOW_MORE
            else getItemViewType2(position)
        } else {
            if (isEven()) {
                if (position == getItemCount2()) ITEM_TYPE_SHOW_MORE
                else getItemViewType2(position)
            } else {
                if (position == getItemCount2() + 1) ITEM_TYPE_SHOW_MORE
                else if (position == getItemCount2()) ITEM_TYPE_EMPTY
                else getItemViewType2(position)
            }
        }
    }


    abstract fun onBindViewHolder2(holder: VH, position: Int)

    abstract fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): VH

    open fun getItemViewType2(position: Int) = 0

    abstract fun getItemCount2(): Int
}