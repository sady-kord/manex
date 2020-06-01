package com.baman.manex.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R

class ProfileDividerViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getInstance(parent: ViewGroup): ProfileDividerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.adapter_profile_divider, parent, false)
            return ProfileDividerViewHolder(
                itemView
            )
        }
    }

    fun bind() {}
}