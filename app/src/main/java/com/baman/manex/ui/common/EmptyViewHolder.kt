package com.baman.manex.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R

class EmptyViewHolder
private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup): EmptyViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            return EmptyViewHolder(
                inflater.inflate(R.layout.item_empty, parent, false)
            )
        }
    }
}