package com.baman.manex.ui.earn

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.baman.manex.R
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.data.dto.StoreInfoDto
import com.baman.manex.ui.common.ListComingSoonViewHolder
import com.baman.manex.ui.common.UnSupportedViewTypeException

class OnlineServiceAdapter(
    val context: Context,
    val isEarn: Boolean,
    val listener: (StoreInfoDto) -> Unit
) :
    DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_COMING_SOON = 0
        const val TYPE_ITEM = 1
    }

    private lateinit var data: List<Any>

    private var comingSoon: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_COMING_SOON -> ListComingSoonViewHolder.getInstance(parent)
            TYPE_ITEM -> {
                OnlineServiceViewHolder.getInstance(
                    parent,
                    isEarn,
                    listener
                )
            }
            else -> throw UnSupportedViewTypeException()
        }

    }

    override fun onCreateLayoutHelper(): GridLayoutHelper {
        val helper = GridLayoutHelper(2)
        val hPadding =
            context.resources.getDimensionPixelSize(R.dimen.main_recycler_horizontalpadding)
        helper.setPadding(hPadding, 0, hPadding, 0)
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ListComingSoonViewHolder -> data[position].let { holder.bind() }
            else -> {
                (holder as OnlineServiceViewHolder).bind(data[position] as StoreInfoDto)
            }
        }

    }

    override fun getItemCount() = if (::data.isInitialized) data.size else 0

    override fun getItemViewType(position: Int): Int {
        val type = if (data[position] is String ) TYPE_COMING_SOON
        else TYPE_ITEM
        return type
    }


    fun setData(
        data: List<OnlineStoreInsideDto>,
        onlineComingSoon: Boolean
    ) {
        this.comingSoon = onlineComingSoon

        if (comingSoon) {

            val l = mutableListOf<Any>()
            l.addAll(data)
            l.add(
               "ComingSoon"
            )
            this.data = l

        } else {
            this.data = data
        }


        notifyDataSetChanged()
    }
}


