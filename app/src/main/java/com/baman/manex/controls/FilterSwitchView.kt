package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.RelativeLayout
import com.baman.manex.R
import kotlinx.android.synthetic.main.view_switch_filter.view.*

class FilterSwitchView : RelativeLayout {

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
        inflater.inflate(R.layout.view_switch_filter, this, true)

    }

    fun setOnSwitchClickListener(touchListener : CompoundButton.OnCheckedChangeListener) {
        _switch.setOnCheckedChangeListener(touchListener)
    }

    fun setSwitchState(state : Boolean) {
        _switch.isChecked = state
    }

}