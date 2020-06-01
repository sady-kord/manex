package com.baman.manex.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundViewHolder
import com.baman.manex.data.dto.*
import com.baman.manex.databinding.AdapterHomeSearchSectionBinding
import com.baman.manex.ui.burn.burnManexStore.search.BurnManexStoreSearchAdapter
import com.baman.manex.ui.burn.burnVoucher.BurnVoucherAdapter
import com.baman.manex.ui.common.ListHeaderViewHolder
import com.baman.manex.ui.earn.earnLocalStore.EarnLocalStoreAdapter
import com.baman.manex.ui.common.UnSupportedViewTypeException
import timber.log.Timber

class HomeSearchSectionAdapter(
    private val itemClickCallback: ((Any,Boolean?,Boolean?) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> ListHeaderViewHolder.getInstance(parent)
            TYPE_ITEM -> {
                val binding: AdapterHomeSearchSectionBinding = DataBindingUtil.inflate<Nothing>(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_home_search_section, parent, false
                )

                SearchSectionViewHolder(
                    binding,
                    appExecutor,
                    itemClickCallback
                )
            }
            else -> throw UnSupportedViewTypeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListHeaderViewHolder -> holder.bind(data[position] as String)
            else -> {
                (holder as SearchSectionViewHolder).bind(data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        val count = if (::data.isInitialized) data.size else 0
        Timber.d("getItemCount: $count")
        return count
    }

    override fun getItemViewType(position: Int): Int {
        val type = if (data[position] is String) TYPE_TITLE
        else TYPE_ITEM
        Timber.d("getItemViewType: $type")
        return type
    }

    private lateinit var data: List<Any>

    private lateinit var appExecutor: AppExecutors

    fun setData(item: Any, appExecutors: AppExecutors) {
        val l = mutableListOf<Any>()
        when (item) {
            is StoresDTO -> {
                if (!item.onLine.branches.isNullOrEmpty()) {
//                    l.add("سرویس آنلاین")
                    l.add(item.onLine)
                }
                if (!item.offline.branches.isNullOrEmpty()) {
//                    l.add("فروشگاه حضوری")
                    l.add(item.offline)
                }
            }
            is BurnDto -> {
                if (!item.onLine.onlines.isNullOrEmpty()) {
//                    l.add("سرویس آنلاین")
                    l.add(item.onLine)
                }
                if (!item.vocher.vochers.isNullOrEmpty()) {
//                    l.add("کارت هدیه")
                    l.add(item.vocher)
                }
                if (!item.manexStore!!.stores!!.isNullOrEmpty()) {
//                    l.add("فروشگاه منکس")
                    l.add(item.manexStore!!)
                }
            }
        }
        data = l
        appExecutor = appExecutors
        notifyDataSetChanged()
    }


    class SearchSectionViewHolder(
        val adapterBinding: AdapterHomeSearchSectionBinding,
        val appExecutors: AppExecutors,
        val itemClickCallback: ((Any,Boolean?,Boolean?) -> Unit)?
    ) :
        DataBoundViewHolder<AdapterHomeSearchSectionBinding>(adapterBinding) {

        fun bind(item: Any) {

            adapterBinding.item = item
            adapterBinding.list.layoutManager =
                LinearLayoutManager(adapterBinding.root.context)

            when (item) {
                is ManexStoreInsideBurnDto -> {
                    var adapter = BurnManexStoreSearchAdapter(appExecutors) {
                        itemClickCallback?.invoke(it!!,null,null)
                    }
                    adapterBinding.list.adapter = adapter
                    adapter.submitList(item.stores)
                }
                is OnlineInsideBurnDto -> {
                    var adapter =
                        HomeSearchOnlineAdapter(
                            appExecutors,
                            false
                        ) {
                            itemClickCallback?.invoke(it, false, true)
                        }
                    adapterBinding.list.adapter = adapter
                    adapter.submitList(item.onlines)
                }
                is VoucherInsideBurnDto -> {
                    var adapter = BurnVoucherAdapter(appExecutors) {
                        itemClickCallback?.invoke(it!!,null,null)
                    }
                    adapterBinding.list.adapter = adapter
                    adapter.submitList(item.vochers)
                }
                is StoresInsideDto -> {
                    var adapter =
                        HomeSearchOnlineAdapter(
                            appExecutors,
                            true
                        ) {
                            itemClickCallback?.invoke(it, true, true)
                        }
                    adapterBinding.list.adapter = adapter
                    adapter.submitList(item.branches)
                }
                is LocalStoresInsideDto -> {
                    var adapter =
                        EarnLocalStoreAdapter {
                            itemClickCallback?.invoke(it!!, true, false)
                        }
                    adapterBinding.list.adapter = adapter
                    adapter.submitList(item.branches)
                }
            }
        }
    }
}