package com.baman.manex.controls

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.baman.manex.R
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.toPersianNumber

class ClearableEditText : AppCompatEditText, OnTouchListener,
    View.OnFocusChangeListener {

    enum class Location(val idx: Int) {
        LEFT(0), RIGHT(2);

    }

    interface Listener {
        fun didClearText()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init()
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    /**
     * null disables the icon
     */
    fun setIconLocation(loc: Location?) {
        this.loc = loc
        initIcon()
    }

    override fun setOnTouchListener(l: OnTouchListener) {
        this.l = l
    }

    override fun setOnFocusChangeListener(f: OnFocusChangeListener) {
        this.f = f
    }

    private var loc: Location? = Location.LEFT

    private var xD: Drawable? = null

    private var listener: Listener? = null

    private var l: OnTouchListener? = null

    private var f: OnFocusChangeListener? = null

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (displayedDrawable != null) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val left =
                if (loc == Location.LEFT) 0 else width - paddingRight - xD!!.intrinsicWidth
            val right =
                if (loc == Location.LEFT) paddingLeft + xD!!.intrinsicWidth else width
            val tappedX =
                x >= left && x <= right && y >= 0 && y <= bottom - top
            if (tappedX) {
                if (event.action == MotionEvent.ACTION_UP) {
                    setText("")
                    if (listener != null) {
                        listener!!.didClearText()
                    }
                }
                return true
            }
        }
        return if (l != null) {
            l!!.onTouch(v, event)
        } else false
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            setClearIconVisible(isNotEmpty(text))
        } else {
            setClearIconVisible(false)
        }
        if (f != null) {
            f!!.onFocusChange(v, hasFocus)
        }
    }

    override fun setCompoundDrawables(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        super.setCompoundDrawables(left, top, right, bottom)
        initIcon()
    }

    private fun init() {
        typeface = ResourcesCompat.getFont(
            context,
            R.font.iranyekan
        )
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)


        requestFocus()
        addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {}
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (isFocused) {
                    setClearIconVisible(isNotEmpty(charSequence))
                }
            }
        })
        initIcon()
        maxLines = 1
        isSingleLine = true
        setLines(1)
        setClearIconVisible(false)


    }

    private fun initIcon() {
        xD = null
        if (loc != null) {
            xD = compoundDrawables[loc!!.idx]
        }
        if (xD == null) {
            xD = ContextCompat.getDrawable(
                context,
                R.drawable.ic_clear
            )
        }
        xD!!.setBounds(0, 0, xD!!.intrinsicWidth, xD!!.intrinsicHeight)
        compoundDrawablePadding = PublicFunction.convertDpToPixels(8f,context)
        val min = paddingTop + xD!!.intrinsicHeight + paddingBottom
        if (suggestedMinimumHeight < min) {
            minimumHeight = min
        }
    }

    private val displayedDrawable: Drawable?
        private get() = if (loc != null) compoundDrawables[loc!!.idx] else null

    protected fun setClearIconVisible(visible: Boolean) {
        val cd = compoundDrawables
        val displayed = displayedDrawable
        val wasVisible = displayed != null
        if (visible != wasVisible) {
            val x = if (visible) xD else null
            super.setCompoundDrawables(
                if (loc == Location.LEFT) x else cd[0],
                cd[1],
                if (loc == Location.RIGHT) x else cd[2],
                cd[3]
            )
        }

    }

    companion object {
        fun isNotEmpty(str: CharSequence?): Boolean {
            return !isEmpty(str)
        }

        fun isEmpty(str: CharSequence?): Boolean {
            return str == null || str.length == 0
        }
    }
}