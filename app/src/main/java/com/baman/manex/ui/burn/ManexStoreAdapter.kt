package com.baman.manex.ui.burn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper
import com.baman.manex.R
import com.baman.manex.controls.CountLabelType
import com.baman.manex.controls.ManexCountLabelControl
import com.baman.manex.data.dto.ManexStoreInsideDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.util.glide.GlideApp

class ManexStoreAdapter(val context: Context,
                        val listener : (ManexStoreInsideDto)-> Unit)
    : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var data: List<ManexStoreInsideDto>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ManexStoreViewHolder(
            inflater.inflate(
                R.layout.adapter_manex_store_item,
                parent,
                false
            ), listener
        )
    }

    override fun onCreateLayoutHelper(): StaggeredGridLayoutHelper {
        val helper = StaggeredGridLayoutHelper(2)
        val hPadding = context.resources.getDimensionPixelSize(R.dimen.main_recycler_horizontalpadding)
        helper.setPadding(hPadding, 0, hPadding, 0)
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ManexStoreViewHolder).bind(data[position])
    }

    override fun getItemCount() = if (::data.isInitialized) data.size else 0

    fun setData(data: List<ManexStoreInsideDto>) {
        this.data = data
        notifyDataSetChanged()
    }
}

class ManexStoreViewHolder(
    itemView: View,
  val  listener: (ManexStoreInsideDto) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val productName: TextView = itemView.findViewById(R.id.manexstore_text_productname)
    private val productModel: TextView = itemView.findViewById(R.id.manexstore_text_productmodel)
    private val manexCountControl: ManexCountLabelControl = itemView.findViewById(R.id.manex_count_control_shop)
    private val productImage: ImageView = itemView.findViewById(R.id.manexstore_image_product)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

    fun bind(item: ManexStoreInsideDto) {
        productName.text = item.name
        productModel.text = item.model

        manexCountControl.setManexCount(item.manexCount)
        manexCountControl.setEntry(RequestType.Burn , CountLabelType.ListItem)

        GlideApp.with(itemView.context).load(item.imageUrl).into(productImage)
        progressBar.progress = item.progressValue

        itemView.setOnClickListener {
            listener.invoke(item)
        }
    }
}
