package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.baman.manex.R
import kotlinx.android.synthetic.main.control_cityitem.view.*
import kotlinx.android.synthetic.main.list_feature.view.*

class CityItemControl : LinearLayoutCompat {


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs, 0)
    }


    private fun initialize(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {


        val inflater: LayoutInflater =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var view: View = inflater.inflate(R.layout.control_cityitem, this, true)


        val a = context!!.obtainStyledAttributes(
            attrs,
            R.styleable.CityItemControl,
            defStyleAttr,
            0
        )

        val value = a.getString(R.styleable.CityItemControl_city_name)
        if (!value!!.isNullOrBlank()) {
            setValue(value)
        }

//        val active = a.getBoolean(R.styleable.CityItemControl_active, false)
//        setActiveCity(active)


        a.recycle()

    }

    fun setValue(value: String) {
        cityTextView.text = value
    }


    fun setActiveCity(value: Boolean) {
        if (!value) {
            marker.setColorFilter(ContextCompat.getColor(context, R.color.gray),
                android.graphics.PorterDuff.Mode.SRC_IN)
            cityTextView.setTextColor(ContextCompat.getColor(context, R.color.gray))
            iconImageView.visibility = View.GONE
        }
    }

    fun setClick(){
        iconImageView.setImageResource(R.drawable.ic_radio_check)
    }

    fun setUnClick(){
        iconImageView.setImageResource(R.drawable.ic_radio_uncheck)
    }


}