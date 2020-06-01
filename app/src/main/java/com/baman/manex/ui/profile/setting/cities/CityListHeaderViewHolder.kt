package com.baman.manex.ui.profile.setting.cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R

class CityListHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getInstance(parent: ViewGroup): CityListHeaderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.adapter_city_listheader, parent, false)
            return CityListHeaderViewHolder(
                itemView
            )
        }
    }

    fun bind(text: String, active: Int) {
        itemView.findViewById<TextView>(R.id.text_header).text = text

        if (active>2) {
            itemView.findViewById<TextView>(R.id.text_header).setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.gray
                )
            )
        }
    }
}