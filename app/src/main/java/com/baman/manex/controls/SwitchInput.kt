package com.baman.manex.controls

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.baman.manex.R
import com.baman.manex.controls.MessageState.Companion.DIM
import com.baman.manex.controls.MessageState.Companion.ERROR
import com.baman.manex.controls.MessageState.Companion.SUCCESS
import kotlinx.android.synthetic.main.input_switch.view.*

class SwitchInput : ConstraintLayout {

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
        inflater.inflate(R.layout.input_switch, this, true)

        val a =
            context!!.obtainStyledAttributes(attrs, R.styleable.SwitchInput, defStyleAttr, 0)

        val text = a.getString(R.styleable.SwitchInput_android_text)
        if (!text.isNullOrBlank()) {
            setText(text)
        }

        val message = a.getString(R.styleable.SwitchInput_message)
        if (!message.isNullOrBlank()) {
            setMessage(message)
        }

        val iconRes = a.getResourceId(R.styleable.SwitchInput_icon, DIM)
        if (iconRes != 0) {
            setIcon(iconRes)
        }

        val initialMessageState = a.getInt(R.styleable.SwitchInput_initialMessageState, 0)
        setMessageState(initialMessageState)

        a.recycle()

        val verticalPadding = resources.getDimensionPixelSize(R.dimen.switchinput_verticalpadding)
        setPadding( 0, verticalPadding, 0, verticalPadding)

        setOnClickListener { _switch.performClick() }
    }

    fun setText(text: String) {
        textView_text.text = text
    }

    fun setText(@StringRes textRes: Int) {
        setText(resources.getString(textRes))
    }

    fun setMessage(message: String) {
        textView_message.text = message
    }

    fun setMessage(@StringRes messageRes: Int) {
        setText(resources.getString(messageRes))
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        setIcon(ContextCompat.getDrawable(context, iconRes))
    }

    fun setIcon(icon: Drawable?) {
        imageView_icon.setImageDrawable(icon)
    }

    fun setMessageState(@MessageState messageState: Int) {
        fun setColor(color: Int) {
            textView_message.setTextColor(ContextCompat.getColor(context, color))
        }
        when (messageState) {
            DIM -> setColor(R.color.input_message_textcolor_dim)
            SUCCESS -> setColor(R.color.input_message_textcolor_success)
            ERROR -> setColor(R.color.input_message_textcolor_error)
        }
    }
}

@IntDef(DIM, SUCCESS, ERROR)
@Retention(AnnotationRetention.SOURCE)
annotation class MessageState {
    companion object {
        const val DIM = 0
        const val SUCCESS = 1
        const val ERROR = 2
    }
}
