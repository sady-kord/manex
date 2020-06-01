package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.baman.manex.util.toPersianNumber


class PersianTextView : AppCompatTextView {

    constructor(context: Context?) : super(context) {
        setInitialize(context!!)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setInitialize(context!!)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setInitialize(context!!)
    }

    private fun setInitialize(context : Context) {

    }

//    override fun setText(text: CharSequence?, type: BufferType?) {
//        if (text != null)
//            text = String.toString().toPersianNumber()
//        super.setText(text, type)
//
//    }
}