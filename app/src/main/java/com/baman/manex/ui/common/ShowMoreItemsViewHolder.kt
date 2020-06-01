package com.baman.manex.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.data.model.ListResultType
import com.baman.manex.util.toPersianNumber

class ShowMoreItemsViewHolder(itemView: View, type: ListResultType): RecyclerView.ViewHolder(itemView) {

    var a = type

    companion object {
        fun getInstance(parent: ViewGroup,type : ListResultType, expandable: (ListResultType) -> Unit): ShowMoreItemsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.adapter_showmore, parent, false)

            itemView.setOnClickListener {
                expandable.invoke(type)
            }

            return ShowMoreItemsViewHolder(itemView,type)
        }
    }

    fun getType(): ListResultType {
        return a
    }

    fun bind(moreItemsCount: Int) {
        val str = moreItemsCount.toString().toPersianNumber()
        itemView.findViewById<TextView>(R.id.text_more).text =
            itemView.context.resources.getString(R.string.adapter_showmore_more, str)
    }
}
