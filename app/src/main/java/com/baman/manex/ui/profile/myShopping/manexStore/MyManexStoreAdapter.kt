package com.baman.manex.ui.profile.myShopping.manexStore

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundViewHolder
import com.baman.manex.controls.AdapterCallBack
import com.baman.manex.data.dto.InvoiceDto
import com.baman.manex.data.model.UserBuyStatus
import com.baman.manex.databinding.AdapterMyManexStoreBinding
import com.baman.manex.ui.common.ListHeaderViewHolder
import com.baman.manex.ui.common.UnSupportedViewTypeException
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber
import timber.log.Timber


class MyManexStoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_ITEM = 1
    }

    var currentPage = 0
    var list = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> ListHeaderViewHolder.getInstance(parent)
            TYPE_ITEM -> {
                val binding: AdapterMyManexStoreBinding = DataBindingUtil.inflate<Nothing>(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_my_manex_store, parent, false
                )
                binding.root.setOnClickListener {

                }

                MyManexStoreViewHolder(binding)

            }
            else -> throw UnSupportedViewTypeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListHeaderViewHolder -> holder.bind(list[position] as String)
            else -> {
                (holder as MyManexStoreViewHolder).bind(list[position] as InvoiceDto)
            }
        }
        checkScrollChanged(itemCount, position)
    }

    override fun getItemCount(): Int {
        val count = list.size
        Timber.d("getItemCount: $count")
        return count
    }

    override fun getItemViewType(position: Int): Int {
        val type = if (list[position] is String) TYPE_TITLE
        else TYPE_ITEM
        Timber.d("getItemViewType: $type")
        return type
    }

    fun setData(data: List<InvoiceDto>,page : Int) {
        val l = mutableListOf<Any>()
//        list.forEach {
//            l.add(it.groupTitle)
//            l.addAll(it.userTransaction)
//        }

        if (currentPage != page || page == 1) {
            currentPage = page
            if (currentPage == 1) {
                list = mutableListOf()
                list.addAll(data)
                notifyDataSetChanged()
            } else {
                list.addAll(list)
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

    class MyManexStoreViewHolder(val adapterBinding: AdapterMyManexStoreBinding) :
        DataBoundViewHolder<AdapterMyManexStoreBinding>(adapterBinding) {

        fun bind(item: InvoiceDto) {

            adapterBinding.manexStore = item

            adapterBinding.storeTitle.text = item.productName
            adapterBinding.storeSubtitle.text = item.subTitle

            adapterBinding.storeManexCount.text = item.manexCount.toString().toPersianNumber()

            adapterBinding.storeTimeOut.text = item.status

            GlideApp.with(adapterBinding.root.context).load(item.imageUrl)
                .into(adapterBinding.voucherImageLogo)


            when (UserBuyStatus.Parse(item.invoiceStatus)) {
                UserBuyStatus.UnSuccess,UserBuyStatus.Expired -> {
                    adapterBinding.voucherState.setBackgroundColor(
                        PublicFunction.getColor(
                            R.color.cancel_color,
                            adapterBinding.root.context
                        )
                    )
                    adapterBinding.storeTimeOut.setTextColor( PublicFunction.getColor(
                        R.color.cancel_color,
                        adapterBinding.root.context
                    ))
                }
                UserBuyStatus.Sent,UserBuyStatus.Deliverd -> {
                    adapterBinding.voucherState.setBackgroundColor(
                        PublicFunction.getColor(
                            R.color.earn_color,
                            adapterBinding.root.context
                        )
                    )
                    adapterBinding.storeTimeOut.setTextColor( PublicFunction.getColor(
                        R.color.earn_color,
                        adapterBinding.root.context
                    ))
                }
                UserBuyStatus.WaitForSend,UserBuyStatus.Init,UserBuyStatus.Valid -> {
                    adapterBinding.voucherState.setBackgroundColor(
                        PublicFunction.getColor(
                            R.color.wait_color,
                            adapterBinding.root.context
                        )
                    )
                    adapterBinding.storeTimeOut.setTextColor( PublicFunction.getColor(
                        R.color.wait_color,
                        adapterBinding.root.context
                    ))
                }
            }

        }

    }

}