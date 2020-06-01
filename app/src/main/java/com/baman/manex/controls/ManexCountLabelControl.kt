package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.baman.manex.R
import com.baman.manex.data.model.RequestType
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber
import kotlinx.android.synthetic.main.control_manex_count_label.view.*

class ManexCountLabelControl : RelativeLayout {

    constructor(context: Context?) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs, defStyleAttr)
    }

    private fun initialize(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {

        val inflater: LayoutInflater =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        inflater.inflate(R.layout.control_manex_count_label, this, true)
    }

    fun setManexCount(count : Int){
        manex_count.text = count.toString().toPersianNumber()
    }

    fun setEntry(status: RequestType, type: CountLabelType, isBlack: Boolean = false) {

        if (type == CountLabelType.ListItem) {
            if (status == RequestType.Earn) {
                GlideApp.with(context).load(context.resources.
                    getDrawable(R.drawable.ic_manex_count_plus)).into(manex_status_image)
                manex_count.setTextColor(context.resources.getColor(R.color.manex_count_earn_item))
                main_layout.background = context.
                    resources.getDrawable(R.drawable.background_manex_count_green)
            } else {
                GlideApp.with(context).load(context.resources.
                    getDrawable(R.drawable.ic_manex_count_minus)).into(manex_status_image)
                manex_count.setTextColor(context.resources.getColor(R.color.manex_count_burn_item))
                main_layout.background = context.
                    resources.getDrawable(R.drawable.background_manex_count_red)
            }
        }else {
          if (!isBlack){
              //all must be white
              if (status == RequestType.Earn) {
                  GlideApp.with(context).load(
                      context.resources.getDrawable(R.drawable.ic_manex_count_plus_white)
                  ).into(manex_status_image)
              }
              else{
                  GlideApp.with(context).load(
                      context.resources.getDrawable(R.drawable.ic_manex_count_minus_white)
                  ).into(manex_status_image)
              }

              manex_count.setTextColor(context.resources.getColor(R.color.white))
              manex_label.setTextColor(context.resources.getColor(R.color.white))
              main_layout.background = context.
                  resources.getDrawable(R.drawable.background_manex_count_white)

          }else{
              if (status == RequestType.Earn){
                  GlideApp.with(context).load(
                      context.resources.getDrawable(R.drawable.ic_manex_count_minus_black)
                  ).into(manex_status_image)
              }else{
                  GlideApp.with(context).load(
                      context.resources.getDrawable(R.drawable.ic_manex_count_minus_black)
                  ).into(manex_status_image)
              }
              manex_count.setTextColor(context.resources.getColor(R.color.colorTextPrimary))
              manex_label.setTextColor(context.resources.getColor(R.color.colorTextPrimary))
              main_layout.background = context.
                  resources.getDrawable(R.drawable.background_manex_count_black)
          }
        }
    }
}

enum class CountLabelType {
    ListItem,
    Collapse
}

