package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.baman.manex.R
import kotlinx.android.synthetic.main.control_home_list_header.view.*
import kotlinx.android.synthetic.main.control_offer_724.view.*

class Offer724Control : LinearLayout {


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

    fun initialize(context: Context?, attrs: AttributeSet?, defStyleAttr: Int?){

        val inflater: LayoutInflater =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var view: View = inflater.inflate(R.layout.control_offer_724, this, true)


        val a = context!!.obtainStyledAttributes(
            attrs,
            R.styleable.Offer724Control,
            defStyleAttr!!,
            0
        )

        val icon = a.getDrawable(R.styleable.Offer724Control_icon_res)
        if (icon != null) {
            view.icon_img.setImageDrawable(icon)
        }

        val title = a.getString(R.styleable.Offer724Control_title_card)
        if (!title.isNullOrEmpty()) {
            settext(title)
        }
        val subTitle = a.getString(R.styleable.Offer724Control_sub_title_card)
        if (!subTitle.isNullOrEmpty()) {
            setSubTitle(subTitle)
        }

        a.recycle()

    }

    private fun setSubTitle(subTitle: String) {
        sub_title_text.text = subTitle
    }

    private fun settext(title: String) {
        title_text.text = title
    }



}