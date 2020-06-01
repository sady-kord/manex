package com.baman.manex.controls

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.baman.manex.R


class TextInputLayoutControl : RelativeLayout {

    constructor(context: Context) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr)
    }

    private lateinit var editText: ClearableEditText

    private fun initialize(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater
            .inflate(R.layout.control_text_input_layout, this, true)

        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.TextInputLayoutControl, 0, 0
        )

        val textInputLayout: CustomTextInputLayout = findViewById(R.id.text_input_layout)

        val hint = a.getString(R.styleable.TextInputLayoutControl_hint)
        if (!hint.isNullOrBlank()) {
            textInputLayout.hint = hint
        }

        val helperText = a.getString(R.styleable.TextInputLayoutControl_helperText)
        if (!helperText.isNullOrBlank()) {
            textInputLayout.helperText = helperText
        }

        val value = a.getString(R.styleable.TextInputLayoutControl_value)
        if (!value.isNullOrBlank()) {
            setValue(value)
        }

        a.recycle()

        editText = v.findViewById(R.id.clear_edit_text)

        layoutDirection = View.LAYOUT_DIRECTION_LTR

        editText.requestFocus()
        editText.isFocusable = true

//        editText.setListener {
//            setValue("")
//        }

    }

    fun addTextChangedListener(listener: TextWatcher) {
        editText.addTextChangedListener(listener)
    }

    fun setValue(text: String?) {
        editText.setText(text)
    }

    fun getValue(): String {
        return editText.text!!.toString()
    }
}
