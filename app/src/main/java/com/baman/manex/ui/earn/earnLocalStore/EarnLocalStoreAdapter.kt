package com.baman.manex.ui.earn.earnLocalStore

import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.controls.AdapterCallBack
import com.baman.manex.data.dto.StoreInfoDto
import com.baman.manex.ui.common.UnSupportedViewTypeException

class EarnLocalStoreAdapter(
    private val itemClickCallback: ((StoreInfoDto?) -> Unit)?
) :
    ListAdapter<Any, RecyclerView.ViewHolder>
        (object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) =
            oldItem as StoreInfoDto == newItem as StoreInfoDto

        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return (oldItem as StoreInfoDto).id == (newItem as StoreInfoDto).id
                    && TextUtils.equals(oldItem.name, newItem.name)
        }

    }) {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_ITEM = 1
    }

    var list = mutableListOf<Any>()
    var currentPage = 0

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is String) TYPE_TITLE else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val instance: RecyclerView.ViewHolder

        when (viewType) {
            TYPE_TITLE -> {
                instance =
                    EarnLocalStoreAddCardViewHolder.getInstance(
                        parent
                    )
            }
            TYPE_ITEM -> {
                instance =
                    EarnLocalStoreViewHolder.getInstance(
                        parent,
                        itemClickCallback
                    )

//                val binding = instance.binding
//
//                binding.root.setOnClickListener {
//                    itemClickCallback!!.invoke(binding.item)
//                }
            }
            else -> throw UnSupportedViewTypeException()
        }
        return instance
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EarnLocalStoreAddCardViewHolder ->
                holder.bind()
            is EarnLocalStoreViewHolder ->
                holder.bind(getItem(position) as StoreInfoDto)
        }
        checkScrollChanged(itemCount, position)
    }

    fun setLocalStoreList(
        haveCard: Boolean,
        listOnline: List<StoreInfoDto>,
        page: Int
    ) {
//        if (!haveCard) {
//            var haveCardItem = ""
//            list.add(haveCardItem)
//            list.addAll(listOnline)
//            super.submitList(list)
//        } else {
//            super.submitList(listOnline)
//        }

        if (currentPage != page || page ==1) {
            currentPage = page
            if (currentPage == 1) {
                list = mutableListOf()
                list.addAll(listOnline)
                super.submitList(list)
            } else {
                list.addAll(listOnline)
                notifyDataSetChanged()
            }
        }
    }

    var lastPosition = -1

    private var adapterCallBack: AdapterCallBack? = null

    fun setAdapterCallBack(myCustomAdapterCallBack: AdapterCallBack?) {
        this.adapterCallBack = myCustomAdapterCallBack
    }

    protected fun checkScrollChanged(adapterSize: Int, position: Int): Int {
        try {
            if (lastPosition >= position) {
                return lastPosition
            }
            lastPosition = position
            if (lastPosition == adapterSize - 1) { // adapterSize - 1 for zero base array
                if (adapterCallBack != null) {
                    adapterCallBack!!.richToEnd()
                }
            }
            return lastPosition
        } catch (ex: Exception) {
            Log.e(
                "ScrollChanged",
                "checkScrollChanged() called with: Exception = " + ex.message + " - Position = [" + position + "]"
            )
            return lastPosition
        }
    }


}