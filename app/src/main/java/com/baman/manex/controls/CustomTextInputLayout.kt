package com.baman.manex.controls


import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.baman.manex.R
import com.google.android.material.textfield.TextInputLayout

open class CustomTextInputLayout : TextInputLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setErrorEnabled(enabled: Boolean) {
        super.setErrorEnabled(enabled)
        if (!enabled) {
            return
        }
        try {
            val errorView = this.findViewById<TextView>(R.id.textinput_error)
            val errorViewParent = errorView.parent as FrameLayout
            errorViewParent.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            errorView.gravity = Gravity.END // replace CENTER  with END or RIGHT
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}