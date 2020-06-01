package com.baman.manex.controls

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.baman.manex.R
import com.baman.manex.data.dto.OtherBranchDto
import com.baman.manex.util.PublicFunction
import kotlinx.android.synthetic.main.view_branch_item.view.*

class BranchItemsView : LinearLayoutCompat {

    private var otherBranches = mutableListOf<OtherBranchDto>()

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context?, attrs: AttributeSet?) {

        orientation = VERTICAL

    }

    fun setItem(otherBranchList: List<OtherBranchDto>) {

        otherBranches = mutableListOf<OtherBranchDto>()

        if (otherBranchList.size > 3)
            for (i in 0 until 2)
                otherBranches.add(otherBranchList[i])
        else
            otherBranches = otherBranchList as MutableList<OtherBranchDto>

        setup()
    }

    private fun setup() {

        removeAllViews()

        val inflater = LayoutInflater.from(context)

        otherBranches.forEach { data ->
            val itemView =
                inflater.inflate(R.layout.view_branch_item, this, false)

            itemView.branch_address_text.text = data.address


            itemView.branch_call_image.setOnClickListener {
                PublicFunction.actionCall(context!!, "02188985421")
            }

            itemView.branch_direction_image.setOnClickListener {
                PublicFunction.shareLocationDialog(context,data.latitude,data.longitude)
            }

            addView(itemView)
        }
    }
}