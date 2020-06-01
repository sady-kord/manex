package com.baman.manex.ui.profile.myShopping.voucher

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundViewHolder
import com.baman.manex.controls.AdapterCallBack
import com.baman.manex.data.dto.InvoiceDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.data.model.UserBuyStatus
import com.baman.manex.databinding.AdapterMyVoucherBinding
import com.baman.manex.ui.common.ListHeaderViewHolder
import com.baman.manex.ui.common.UnSupportedViewTypeException
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber
import timber.log.Timber

class MyVoucherAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemCallBack: MyVoucherAdapterCallBack? = null

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
                val binding: AdapterMyVoucherBinding = DataBindingUtil.inflate<Nothing>(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_my_voucher, parent, false
                )
                binding.root.setOnClickListener {
                    itemCallBack?.itemSelectedCallBack(binding.voucherItem!!.id)
                }

                MyVoucherViewHolder(binding)

            }
            else -> throw UnSupportedViewTypeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListHeaderViewHolder -> holder.bind(list[position] as String)
            else -> {
                (holder as MyVoucherViewHolder).bind(list[position] as InvoiceDto)
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


    fun setData(data: List<InvoiceDto>, page: Int) {

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

    class MyVoucherViewHolder(val adapterBinding: AdapterMyVoucherBinding) :
        DataBoundViewHolder<AdapterMyVoucherBinding>(adapterBinding) {

        fun bind(item: InvoiceDto) {

            adapterBinding.voucherItem = item

            adapterBinding.voucherTitle.text = item.productName
            adapterBinding.voucherSubTitle.text = item.subTitle

            adapterBinding.voucherManexCount.text = item.manexCount.toString().toPersianNumber()


            if (!item.invoiceType.isNullOrEmpty()) {
                when (RequestType.valueOf(item.invoiceType)) {
                    RequestType.Burn -> {
                        adapterBinding.voucherManexCount.setTextColor(
                            PublicFunction.getColor(R.color.red, adapterBinding.root.context)
                        )
                    }
                    RequestType.Earn -> {
                        adapterBinding.voucherManexCount.setTextColor(
                            PublicFunction.getColor(R.color.earn_color, adapterBinding.root.context)
                        )
                    }
                }
            }



            adapterBinding.voucherTimeOut.text = item.status

            GlideApp.with(adapterBinding.root.context).load(item.imageUrl)
                .into(adapterBinding.voucherImageLogo)

            when (UserBuyStatus.Parse(item.invoiceStatus)) {
                UserBuyStatus.UnSuccess, UserBuyStatus.Expired -> {
                    adapterBinding.voucherState.setBackgroundColor(
                        PublicFunction.getColor(
                            R.color.cancel_color,
                            adapterBinding.root.context
                        )
                    )
                }
                UserBuyStatus.Sent, UserBuyStatus.Deliverd, UserBuyStatus.Valid -> {
                    adapterBinding.voucherState.setBackgroundColor(
                        PublicFunction.getColor(
                            R.color.earn_color,
                            adapterBinding.root.context
                        )
                    )
                }
                UserBuyStatus.WaitForSend, UserBuyStatus.Init -> {
                    adapterBinding.voucherState.setBackgroundColor(
                        PublicFunction.getColor(
                            R.color.wait_color,
                            adapterBinding.root.context
                        )
                    )
                }
            }


        }

    }

    fun setItemSelectedCallBack(callBack: MyVoucherAdapterCallBack) {
        itemCallBack = callBack
    }

    interface MyVoucherAdapterCallBack {
        fun itemSelectedCallBack(id: Long)
    }

}
