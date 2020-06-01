package com.baman.manex.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R

class ListHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getInstance(parent: ViewGroup): ListHeaderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.adapter_listheader, parent, false)
            return ListHeaderViewHolder(itemView)
        }
    }

    fun bind(text: String) {
        itemView.findViewById<TextView>(R.id.text_header).text = text
    }
}
