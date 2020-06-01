package com.baman.manex.ui.common

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R

class DividerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        fun getInstance(parent: ViewGroup) = SimpleViewHolder.getInstance(parent, R.layout.adapter_divideritem)
    }
}