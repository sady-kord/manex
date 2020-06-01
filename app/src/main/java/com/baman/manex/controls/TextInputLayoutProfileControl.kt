package com.baman.manex.controls

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.baman.manex.R
import com.baman.manex.util.PublicFunction
import kotlinx.android.synthetic.main.control_text_input_layout_profile.view.*


class TextInputLayoutProfileControl : LinearLayout {

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

    private lateinit var textInputLayout: CustomTextInputLayout


    private fun initialize(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater
            .inflate(R.layout.control_text_input_layout_profile, this, true)

        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.TextInputLayoutProfileControl, defStyleAttr, 0
        )

        textInputLayout = findViewById(R.id.text_input_layout)

        val hint = a.getString(R.styleable.TextInputLayoutProfileControl_hintText)
        if (!hint.isNullOrBlank()) {
            textInputLayout.hint = hint
        }

        val helperText = a.getString(R.styleable.TextInputLayoutProfileControl_helpText)
        if (!helperText.isNullOrBlank()) {
            textInputLayout.helperText = helperText
        }

        val value = a.getString(R.styleable.TextInputLayoutProfileControl_value)
        if (!value.isNullOrBlank()) {
            setValue(value)
        }

        a.getDrawable(R.styleable.TextInputLayoutProfileControl_pic)?.let {
            setIcon(it)
        }

        a.getColor(
            R.styleable.TextInputLayoutProfileControl_boxBackground, ContextCompat.getColor(
                context,
                R.color.main_navhost_backgroundcolor
            )
        ).let {
            setbackgroundColor(it)
        }

        val isBirthDay = a.getBoolean(R.styleable.TextInputLayoutProfileControl_isBirthday, false)

        setIsBirthDay(isBirthDay)

        a.recycle()

        editText = v.findViewById(R.id.clear_edit_text)

        layoutDirection = View.LAYOUT_DIRECTION_RTL

        editText.requestFocus()
        editText.isFocusable = true

//        editText.setListener {
//            //            setValue("")
//        }

    }

    private fun setIsBirthDay(birthDay: Boolean) {
        if (birthDay) {
            frame.visibility = View.VISIBLE
        } else {
            frame.visibility = View.GONE
        }
    }

    fun addTextChangedListener(listener: TextWatcher) {
        editText.addTextChangedListener(listener)
    }

    fun setValue(text: String?) {
        editText.setText(text)
    }

    fun setIcon(id: Drawable) {
        icon_img.setBackgroundDrawable(id)
    }

    fun getValue(): String {
        return editText.text!!.toString()
    }

    fun setNumericKeyboard() {
        clear_edit_text.inputType = InputType.TYPE_CLASS_NUMBER
        clear_edit_text.layoutDirection = View.LAYOUT_DIRECTION_LTR
    }

    fun setMaxLenght(maxLimit: Int) {
        clear_edit_text.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLimit))
    }

    fun setbackgroundColor(it: Int) {
        textInputLayout.boxBackgroundColor = it
    }
}
