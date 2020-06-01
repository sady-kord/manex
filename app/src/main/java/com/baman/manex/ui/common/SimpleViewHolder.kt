package com.baman.manex.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class SimpleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        fun getInstance(parent: ViewGroup, @LayoutRes itemViewResource: Int): SimpleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(itemViewResource, parent, false)
            return SimpleViewHolder(itemView)
        }
    }
}