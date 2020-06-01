package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.baman.manex.R
import com.baman.manex.util.toPersianNumber
import com.jaygoo.widget.OnRangeChangedListener
import kotlinx.android.synthetic.main.view_manex_range_filter.view.*
import timber.log.Timber


class FilterManexRangeView : LinearLayout {

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
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_manex_range_filter, this, true)

        setup()
    }

    private fun setup() {


    }

    private var hasSetLimits = false
    private var pendingMinValue = 0
    private var pendingMaxValue = 0

    fun setLimits(minLimit: Int, maxLimit: Int) {
        hasSetLimits = true

        seekbar.setRange(minLimit.toFloat(),maxLimit.toFloat(),10f)
        seekbar.setProgress(minLimit.toFloat(),maxLimit.toFloat())

        manex_count_start.text = minLimit.toString().toPersianNumber()
        manex_count_end.text = maxLimit.toString().toPersianNumber()

        if (pendingMinValue != 0 && pendingMaxValue !=0) {
            seekbar.setProgress(pendingMinValue.toFloat(),pendingMaxValue.toFloat())
        }

        if (pendingMaxValue != 0 && pendingMinValue == 0) {
            seekbar.setProgress(minLimit.toFloat(),pendingMaxValue.toFloat())
        }

        if (pendingMinValue != 0 && pendingMaxValue == 0) {
            seekbar.setProgress(pendingMinValue.toFloat(),maxLimit.toFloat())
        }

    }

    fun setManexRangeValue(minValue : Int , maxValue : Int) {
        Timber.d("minValue: $minValue, maxValue: $maxValue")

        if (!hasSetLimits) {
            pendingMinValue = minValue
            pendingMaxValue = maxValue
        } else {
            seekbar.setProgress(minValue.toFloat(),maxValue.toFloat())
        }
    }

    fun setOnTrackingChangeListener(multiSlider :  OnRangeChangedListener) {
        seekbar.setOnRangeChangedListener(multiSlider)
    }

}