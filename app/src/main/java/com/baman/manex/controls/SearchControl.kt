package com.baman.manex.controls

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.baman.manex.R


class SearchControl : SearchView {

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

        background = ContextCompat.getDrawable(context!!, R.drawable.view_searchcontrol_background)

        setIconifiedByDefault(false)

        val v: View = findViewById(androidx.appcompat.R.id.search_plate)
        v.setBackgroundColor(Color.TRANSPARENT)


    }
}