package com.baman.manex.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R

class ListComingSoonViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getInstance(parent: ViewGroup): ListComingSoonViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.adapter_coming_soon, parent, false)
            return ListComingSoonViewHolder(itemView)
        }
    }

    fun bind() {
    }
}
