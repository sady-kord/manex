package com.baman.manex.ui.burn

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import com.baman.manex.data.dto.VoucherInsideDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber

class VoucherAdapter (val context: Context,
                      val listener : (VoucherInsideDto)-> Unit)
    : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var data: List<VoucherInsideDto>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        VoucherViewHolder.newInstance(
            parent,
            listener
        )

    override fun onCreateLayoutHelper(): LinearLayoutHelper{
        val helper = LinearLayoutHelper()
        val hPadding = context.resources.getDimensionPixelSize(R.dimen.main_recycler_horizontalpadding)
        helper.setPadding(hPadding, 0, hPadding, 0)
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VoucherViewHolder).bind(data[position])
    }

    override fun getItemCount() = if (::data.isInitialized) data.size else 0

    fun setData(items: List<VoucherInsideDto>) {
        data = items
        notifyDataSetChanged()
    }
}

class VoucherViewHolder(
    itemView: View,
    val listener: (VoucherInsideDto) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val text_name: TextView = itemView.findViewById(R.id.text_name)
    private val text_subtitle: TextView = itemView.findViewById(R.id.text_subtitle)
    private val manex_count_control: ManexCountLabelControl = itemView.findViewById(R.id.manex_count_control_voucher)
    private val text_expire_time: TextView = itemView.findViewById(R.id.text_expire_time)
    private val progress_bar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    private val item_image: AppCompatImageView = itemView.findViewById(R.id.item_image)


    fun bind(item: VoucherInsideDto) {
        text_name.text = item.title.toPersianNumber()
        text_subtitle.text = item.subTitle

        manex_count_control.setManexCount(item.manexCount)
        manex_count_control.setEntry(RequestType.Burn , CountLabelType.ListItem)


        text_expire_time.text = item.expiryText.toPersianNumber()
        progress_bar.progress = item.progressValue


        GlideApp.with(itemView.context).load(item.imageUrl).into(item_image)

        itemView.setOnClickListener {
            listener.invoke(item)
        }
    }

    companion object {
        fun newInstance(
            parent: ViewGroup,
            listener: (VoucherInsideDto) -> Unit
        ): VoucherViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(
                R.layout.adapter_voucher_code_item,
                parent,
                false
            )

            return VoucherViewHolder(itemView, listener)
        }
    }
}
