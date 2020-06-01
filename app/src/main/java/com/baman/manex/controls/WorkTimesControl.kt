package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.baman.manex.R
import kotlinx.android.synthetic.main.control_work_time.view.*

class WorkTimesControl(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.control_work_time, this, true)

    }

    fun setTitle(title : String) {
        title_text.text = title
    }

    fun setTime(time : String) {
        time_text.text = time
    }
}