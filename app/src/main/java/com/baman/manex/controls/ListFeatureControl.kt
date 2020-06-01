package com.baman.manex.controls

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.baman.manex.R
import com.baman.manex.util.PublicFunction
import kotlinx.android.synthetic.main.list_feature.view.*

class ListFeatureControl : RelativeLayout {


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

        var view: View = inflater.inflate(R.layout.list_feature, this, true)


        val a = context!!.obtainStyledAttributes(
            attrs,
            R.styleable.ListFeatureControl,
            defStyleAttr!!,
            0
        )

        val showSort = a.getBoolean(R.styleable.ListFeatureControl_showSort,true)
        if (!showSort) {
            root.weightSum = 2f
            view.sort_view.visibility = View.GONE
            view.sort.visibility = View.GONE
        }

        a.recycle()

    }

    fun setSortSelected(isSelected : Boolean){
        if (isSelected) {
            sort_selector_view.visibility = View.VISIBLE
            sort_text.setTextColor(
                PublicFunction.getColor(
                    R.color.list_feature_selected_color,
                    context
                )
            )
            icon_sort_image.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.list_feature_selected_color
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }else{
            sort_selector_view.visibility = View.GONE
            sort_text.setTextColor(
                PublicFunction.getColor(
                    R.color.black,
                    context
                )
            )
            icon_sort_image.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.black
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    fun setFilterSelected(isSelected : Boolean){
        if (isSelected){
            filter_selector_view.visibility = View.VISIBLE
            filter_text.setTextColor(PublicFunction.getColor(R.color.list_feature_selected_color,context))
            icon_filter_image.setColorFilter(ContextCompat.getColor(context, R.color.list_feature_selected_color), android.graphics.PorterDuff.Mode.SRC_IN)
        }else{
            filter_selector_view.visibility = View.GONE
            filter_text.setTextColor(PublicFunction.getColor(R.color.black,context))
            icon_filter_image.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
        }

    }

    fun setSearchSelected(isSelected : Boolean){
        if (isSelected){
            search_selector_view.visibility = View.VISIBLE
            search_text.setTextColor(PublicFunction.getColor(R.color.list_feature_selected_color,context))
            icon_search_image.setColorFilter(ContextCompat.getColor(context, R.color.list_feature_selected_color), android.graphics.PorterDuff.Mode.SRC_IN)
        }else{
            search_selector_view.visibility = View.GONE
            search_text.setTextColor(PublicFunction.getColor(R.color.black,context))
            icon_search_image.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

}
