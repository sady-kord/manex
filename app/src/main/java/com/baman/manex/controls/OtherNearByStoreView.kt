package com.baman.manex.controls

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.baman.manex.R
import com.baman.manex.data.dto.OnlineStoreInsideDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber
import kotlinx.android.synthetic.main.adapter_local_store.view.*
import kotlinx.android.synthetic.main.adapter_local_store.view.icon_image_view
import kotlinx.android.synthetic.main.adapter_local_store.view.localstore_address_text
import kotlinx.android.synthetic.main.adapter_local_store.view.localstore_amount_text
import kotlinx.android.synthetic.main.adapter_local_store.view.localstore_name_text
import kotlinx.android.synthetic.main.view_othernearbystore.view.*
import java.util.*

class OtherNearByStoreView : LinearLayoutCompat {

    private var onlineStoreInsides: ArrayList<OnlineStoreInsideDto> =
        ArrayList<OnlineStoreInsideDto>()

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context?, attrs: AttributeSet?) {
        orientation = VERTICAL
    }

    fun setItem(storeList: List<OnlineStoreInsideDto>) {
        if (storeList.size > 3) {
            for (i in storeList.indices) {
                if (i < 3) {
                    onlineStoreInsides.add(storeList[i])
                }
            }
        } else {
            for (i in storeList.indices) {
                onlineStoreInsides.add(storeList[i])
            }
        }
        setup()
    }

    fun clearItem() {
        onlineStoreInsides.clear()
    }

    private fun setup() {
        removeAllViews()
        val inflater = LayoutInflater.from(context)

        onlineStoreInsides.forEach { item ->

            val itemView =
                inflater.inflate(R.layout.view_othernearbystore, this, false)

            val amountValue = item.earnManexConditionTitle!!.price.toString().toPersianNumber()
            itemView.localstore_amount_text.text = "هر $amountValue هزار تومان"

            itemView.manex_count_control_near_branch.
                setManexCount(item.earnManexConditionTitle!!.manexCount)
            itemView.manex_count_control_near_branch.setEntry(
                RequestType.Earn,
                CountLabelType.ListItem
            )

            itemView.localstore_name_text.text = item.name
            itemView.localstore_address_text.text = item.address
            GlideApp.with(itemView.context).load(item.imagePath).into(itemView.icon_image_view)

            addView(itemView)
        }

    }
}