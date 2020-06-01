package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.baman.manex.R
import com.baman.manex.util.PublicFunction
import kotlinx.android.synthetic.main.control_home_list_header.view.*
import kotlinx.android.synthetic.main.list_feature.view.*

class HomeListHeaderControl : LinearLayout {


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

        var view: View = inflater.inflate(R.layout.control_home_list_header, this, true)


        val a = context!!.obtainStyledAttributes(
            attrs,
            R.styleable.HomeListHeaderControl,
            defStyleAttr!!,
            0
        )

        val showSort = a.getBoolean(R.styleable.HomeListHeaderControl_showMore,true)
        if (!showSort) {
            show_more_text.visibility = View.GONE
        }

        val title = a.getString(R.styleable.HomeListHeaderControl_title)
        if (!title.isNullOrEmpty()) {
            settext(title)
        }

        a.recycle()

    }

    private fun settext(title: String) {
        header_title_text.text = title
    }

}