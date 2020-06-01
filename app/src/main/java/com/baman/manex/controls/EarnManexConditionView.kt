package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.baman.manex.R
import com.baman.manex.data.dto.ManexConditionsDto
import com.baman.manex.util.toPersianNumber
import kotlinx.android.synthetic.main.view_earn_manex_condition.view.*

class EarnManexConditionView : LinearLayoutCompat {

    private var earnConditions = mutableListOf<ManexConditionsDto>()

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

        orientation = VERTICAL

    }

    fun setItem(conditionList: List<ManexConditionsDto>, isEarn: Boolean) {

        earnConditions = conditionList as MutableList<ManexConditionsDto>

        setup(isEarn)
    }

    private fun setup(earn: Boolean) {
        removeAllViews()

        val inflater = LayoutInflater.from(context)

        earnConditions.forEach { data ->
            val itemView =
                inflater.inflate(R.layout.view_earn_manex_condition, this, false)

            if (!earn)
                itemView.earn_title.text = "با خرج "
            else
                itemView.earn_title.text = "کسب "

                itemView.manex_count.text = data.manexCount.toString().toPersianNumber()
            itemView.earn_manex_price.text = data.title

            addView(itemView)
        }
    }
}