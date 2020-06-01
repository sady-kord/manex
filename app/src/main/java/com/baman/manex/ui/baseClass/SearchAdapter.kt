package com.baman.manex.ui.baseClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.data.dto.BranchListDto
import com.baman.manex.data.dto.StoreInfoDto
import com.baman.manex.data.model.ListResultType
import com.baman.manex.ui.common.DividerViewHolder
import com.baman.manex.ui.common.ListHeaderViewHolder
import com.baman.manex.ui.common.ShowMoreItemsViewHolder
import com.baman.manex.ui.common.UnSupportedViewTypeException
import com.baman.manex.ui.earn.LocalStoreViewHolder
import timber.log.Timber


class SearchAdapter(
    val listener: (StoreInfoDto) -> Unit,
    val showMoreListener: (ListResultType, Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private class ItemType(val type: Int, val count: Int = 1)

    companion object {
        private const val TYPE_ITEM_BY_NAME = 0
        private const val TYPE_ITEM_BY_ADDRESS = 1
        private const val TYPE_ITEM_TAG_RESULT = 6
        private const val TYPE_DIVIDER = 2
        private const val TYPE_SHOW_MORE_ADDRESS = 3
        private const val TYPE_SHOW_MORE_NAME = 7

        private const val TYPE_TITLE = 5
    }

    private val nameSearchResult = mutableListOf<StoreInfoDto>()
    private val addressSearchResult = mutableListOf<StoreInfoDto>()
    private val tagSearchResult = mutableListOf<StoreInfoDto>()
    private val itemTypeList = mutableListOf<ItemType>()


    private var nameShowMoreCount = 0
    private var addressShowMoreCount = 0

    private var nameShowMorePage = 1
    private var addressShowMorePage = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM_BY_NAME, TYPE_ITEM_BY_ADDRESS, TYPE_ITEM_TAG_RESULT ->
                LocalStoreViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.adapter_local_store,
                        parent,
                        false
                    ),
                    listener
                )
            TYPE_DIVIDER -> DividerViewHolder.getInstance(parent)
            TYPE_SHOW_MORE_ADDRESS -> {
                ShowMoreItemsViewHolder.getInstance(
                    parent,
                    ListResultType.SearchByAddress
                ) {
                    showMoreListener.invoke(it, addressShowMorePage)
                    addressShowMorePage++
                }
            }
            TYPE_SHOW_MORE_NAME -> {
                ShowMoreItemsViewHolder.getInstance(
                    parent,
                    ListResultType.SearchByName
                ) {
                    showMoreListener.invoke(it, nameShowMorePage)
                    nameShowMorePage++
                }
            }
            TYPE_TITLE -> ListHeaderViewHolder.getInstance(parent)
            else -> throw UnSupportedViewTypeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocalStoreViewHolder -> {
                var offset = 0
                for (itemType in itemTypeList) {
                    when (itemType.type) {
                        TYPE_ITEM_TAG_RESULT -> {
                            holder.bind(tagSearchResult[position])
                            return
                        }
                        TYPE_ITEM_BY_NAME -> {
                            val itemCount = itemType.count
                            if (position - offset <= itemCount) {
                                holder.bind(nameSearchResult[position - offset])
                                return
                            } else offset += itemCount
                        }
                        TYPE_ITEM_BY_ADDRESS -> {
                            holder.bind(addressSearchResult[position - offset])
                            return
                        }
                        TYPE_DIVIDER, TYPE_SHOW_MORE_ADDRESS, TYPE_TITLE, TYPE_SHOW_MORE_NAME -> offset++
                        else -> throw UnSupportedViewTypeException()
                    }
                }
            }

            is ShowMoreItemsViewHolder -> {

                when (holder.getType()) {
                    ListResultType.SearchByName -> {
                        holder.bind(nameShowMoreCount)
                    }
                    ListResultType.SearchByAddress -> {
                        holder.bind(addressShowMoreCount)
                    }
                }

            }

            is ListHeaderViewHolder -> {
                val titleRes: Int = if (nameSearchResult.isNotEmpty() && position > 0) {
                    R.string.offlinestoresearch_listheader_address
                } else {
                    R.string.offlinestoresearch_listheader_name
                }

                holder.bind(holder.itemView.context.getString(titleRes))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val type = getType(position) ?: throw UnSupportedViewTypeException()
        Timber.d("get item view type for position: $position is: $type")
        return type
    }

    private fun getType(position: Int): Int? {
        if (tagSearchResult.size > 0) return TYPE_ITEM_TAG_RESULT
        var c = 0
        for (itemType in itemTypeList) {
            c += itemType.count
            if (position < c) return itemType.type
        }
        return null
    }

    override fun getItemCount(): Int {
        if (tagSearchResult.size > 0) return tagSearchResult.size
        var itemCount = 0
        for (itemType in itemTypeList) {
            itemCount += itemType.count
        }
        Timber.d("item count: $itemCount")
        return itemCount
    }

    private fun clearData() {
        addressSearchResult.clear()
        nameSearchResult.clear()
        tagSearchResult.clear()
        nameShowMoreCount = 0
        addressShowMoreCount = 0
        nameShowMorePage = 1
        addressShowMorePage = 1
    }

    fun setData(items: List<BranchListDto>) {

//        clearData()

        items.forEach {
            when (ListResultType.Parse(it.objectType)) {
                ListResultType.Basic -> {
                    tagSearchResult.addAll(it.branches)
                }
                ListResultType.SearchByName -> {
                    nameSearchResult.addAll(it.branches)
                    nameShowMoreCount = it.count
                }
                ListResultType.SearchByAddress -> {
                    addressSearchResult.addAll(it.branches)
                    addressShowMoreCount = it.count
                }
            }
        }

        setItemTypeList()
        notifyDataSetChanged()
    }

    private fun setItemTypeList() {
        itemTypeList.clear()
        val tagSearchResultSize = tagSearchResult.size
        if (tagSearchResultSize > 0) {
            itemTypeList.add(
                ItemType(
                    TYPE_ITEM_TAG_RESULT,
                    tagSearchResultSize
                )
            )
            return
        }

        val nameResultSize = nameSearchResult.size
        if (nameResultSize > 0) {
            itemTypeList.add(
                ItemType(
                    TYPE_TITLE
                )
            )

            if (nameShowMoreCount != 0) {
                itemTypeList.add(
                    ItemType(
                        TYPE_ITEM_BY_NAME,
                        nameResultSize
                    )
                )
                itemTypeList.add(
                    ItemType(
                        TYPE_SHOW_MORE_NAME
                    )
                )
            } else {
                itemTypeList.add(
                    ItemType(
                        TYPE_ITEM_BY_NAME,
                        nameResultSize
                    )
                )
            }

        }

        val addressResultSize = addressSearchResult.size
        if (addressResultSize > 0) {
            if (nameResultSize > 0) {
                itemTypeList.add(
                    ItemType(
                        TYPE_DIVIDER
                    )
                )
            }
            itemTypeList.add(
                ItemType(
                    TYPE_TITLE
                )
            )


            if (addressShowMoreCount != 0) {
                itemTypeList.add(
                    ItemType(
                        TYPE_ITEM_BY_ADDRESS,
                        addressResultSize
                    )
                )
                itemTypeList.add(
                    ItemType(
                        TYPE_SHOW_MORE_ADDRESS
                    )
                )
            } else {
                itemTypeList.add(
                    ItemType(
                        TYPE_ITEM_BY_ADDRESS,
                        addressResultSize
                    )
                )
            }

        }
    }

    fun clear() {
        setData(emptyList())
    }
}

