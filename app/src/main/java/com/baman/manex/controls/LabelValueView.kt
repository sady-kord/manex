package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.baman.manex.R
import kotlinx.android.synthetic.main.view_labelvalue.view.*

class LabelValueView: ConstraintLayout {
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
        inflater.inflate(R.layout.view_labelvalue, this, true)

        val a = context!!.obtainStyledAttributes(
            attrs,
            R.styleable.LabelValueView,
            defStyleAttr,
            0
        )

        val label = a.getString(R.styleable.LabelValueView_label)
        if (label?.isNotBlank() == true) {
            setLabel(label)
        }

        val value = a.getString(R.styleable.LabelValueView_value)
        if (value?.isNotBlank() == true) {
            setValue(value)
        }

        val textSize = a.getResourceId(R.styleable.LabelValueView_android_textSize, 0)
        if (textSize != 0) {
            setTextSize(textSize)
        }

        val color = a.getResourceId(R.styleable.DirectLabel_textColor, 0)
        if (color != 0) {
            setTextColor(color)
        }

        a.recycle()

    }

    fun setLabel(@StringRes label: Int) {
        setLabel(resources.getString(label))
    }

    fun setLabel(label: CharSequence) {
        textView_label.text = label
    }

    fun setValue(@StringRes value: Int) {
        setValue(resources.getString(value))
    }

    fun setValue(value: CharSequence) {
        textView_value.text = value
    }

    fun setTextSize(@DimenRes textSize: Int) {
        textView_label.textSize = textSize.toFloat()
        textView_value.textSize = textSize.toFloat()
    }

    fun setTextColor(@ColorRes textColor: Int) {
        textView_label.setTextColor(ContextCompat.getColor(context, textColor))
        textView_value.setTextColor(ContextCompat.getColor(context, textColor))
    }
}