package com.baman.manex.ui.earn

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.baman.manex.R
import com.baman.manex.controls.CountLabelType
import com.baman.manex.controls.ManexCountLabelControl
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.data.dto.StoreInfoDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class LocalStoreAdapter(
    val context: Context,
    val listener: (StoreInfoDto) -> Unit,
    val addCardListener: () -> Unit
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_TYPE_HEADER = 0
        private const val ITEM_TYPE_ITEMS = 1
    }

    private lateinit var data: List<StoreInfoDto>
    private var showHeader = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_TYPE_HEADER ->
                LocalStoreHeaderViewHolder(
                    inflater.inflate(R.layout.adapter_add_card_item, parent, false), addCardListener
                )
            ITEM_TYPE_ITEMS ->
                LocalStoreViewHolder(
                    inflater.inflate(R.layout.adapter_local_store, parent, false), listener
                )
            else -> throw IllegalStateException("View type not supported.")
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (showHeader && position == 0) ITEM_TYPE_HEADER else ITEM_TYPE_ITEMS

    override fun getItemCount(): Int {
        return if (::data.isInitialized) {

            if (showHeader)
                data.size + 1
            else
                data.size

        } else 0
    }

    override fun onCreateLayoutHelper(): LinearLayoutHelper {
        val helper = LinearLayoutHelper()
        val hPadding =
            context.resources.getDimensionPixelSize(R.dimen.main_recycler_horizontalpadding)
        helper.setPadding(hPadding, 0, hPadding, 0)
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is LocalStoreHeaderViewHolder -> {
                holder.bind()
            }

            is LocalStoreViewHolder ->
                    holder.bind(data[position])

        }
    }

    fun setData(
        data: List<StoreInfoDto>,
        showHeader: Boolean
    ) {
        this.showHeader = showHeader // todo: fix this
        this.data = data
        notifyDataSetChanged()
    }
}

class LocalStoreViewHolder(
    itemView: View,
    val listener: (StoreInfoDto) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val storeName: TextView = itemView.findViewById(R.id.localstore_name_text)
    private val address: TextView = itemView.findViewById(R.id.localstore_address_text)
    private val amount: TextView = itemView.findViewById(R.id.localstore_amount_text)
    private val manexCount: ManexCountLabelControl = itemView.findViewById(R.id.manex_count_control)
    private val iconImageView: AppCompatImageView = itemView.findViewById(R.id.icon_image_view)

    fun bind(item: StoreInfoDto) {
        val amountValue = item.earnManexConditionTitle.title.toPersianNumber()
        amount.text = amountValue

//        val manexCountText =
//            item.earnManexConditionTitle!!.manexCount.toString()?.toPersianNumber()
//        val text =
//            itemView.context.getString(
//                R.string.onlineadapteritem_subtitle_format,
//                manexCountText
//            )
//        val ssb = SpannableStringBuilder(text)
//        val startingIndex = text.indexOf(manexCountText)
//        val colorSpan = ForegroundColorSpan(
//            ContextCompat.getColor(
//                itemView.context,
//                R.color.adaptertem_manexcount_textcolor
//            )
//        )
//        ssb.setSpan(
//            colorSpan,
//            startingIndex,
//            startingIndex + manexCountText.length,
//            Spanned.SPAN_INCLUSIVE_INCLUSIVE
//        )
//
//        val typeface = ResourcesCompat.getFont(itemView.context, R.font.iranyekan_bold)
//
//        ssb.setSpan(
//            CustomTypefaceSpan("", typeface),
//            startingIndex, startingIndex + manexCountText.length,
//            Spanned.SPAN_INCLUSIVE_INCLUSIVE
//        )
//
//        manexCount.text = PublicFunction.increaseFontSizeForPath(ssb, manexCountText, 1.25f)

        manexCount.setManexCount(item.earnManexConditionTitle!!.manexCount)
        manexCount.setEntry(RequestType.Earn , CountLabelType.ListItem)


        storeName.text = item.name
        address.text = item.address
        GlideApp.with(itemView.context).load(item.imagePath).into(iconImageView)

        itemView.setOnClickListener {
            listener.invoke(item)
        }


    }
}

class LocalStoreHeaderViewHolder(itemView: View, val listener: () -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    //
    fun bind() {
        itemView.setOnClickListener {
            listener.invoke()
        }
    }
}