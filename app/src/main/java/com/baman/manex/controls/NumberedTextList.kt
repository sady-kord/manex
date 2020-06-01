package com.baman.manex.controls

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.baman.manex.R
import com.baman.manex.util.CompatibleTypefaceSpan
import com.baman.manex.util.ext.pixelsToSp
import com.baman.manex.util.toPersianNumber


class NumberedTextList : LinearLayoutCompat {

    companion object {
        const val INDICATOR_TYPE_NUMBER = 0
        const val INDICATOR_TYPE_BULLET = 1
    }

    @IntDef(
        INDICATOR_TYPE_NUMBER,
        INDICATOR_TYPE_BULLET
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class IndicatorType

    private val defaultIndicatorSeparator = "."

    private var textList = emptyList<CharSequence>()
    private var textColor = 0

    private var textSize = 0f
    private var indicatorSeparator = defaultIndicatorSeparator
    private var indicatorType = 0
    private var indicatorColor = 0
    private var textMargin = 0
    private var itemMargin = 0

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

        orientation = VERTICAL

        val a = context!!.obtainStyledAttributes(
            attrs,
            R.styleable.NumberedTextList,
            defStyleAttr,
            0
        )

        val text = a.getText(R.styleable.NumberedTextList_android_text)
        if (!text.isNullOrBlank()) {
            this.textList = text.split("\n")
        }

        val defaultTextColor = R.color.colorPrimaryDark

        val color =
            a.getResourceId(R.styleable.NumberedTextList_android_textColor, defaultTextColor)
        textColor = ContextCompat.getColor(context, color)

        val defaultTextSize = resources.getDimension(R.dimen.numberedtextlist_textsize)
        textSize = resources.pixelsToSp(
            a.getDimension(
                R.styleable.NumberedTextList_android_textSize,
                defaultTextSize
            )
        )

        val defTextMargin =
            resources.getDimensionPixelSize(R.dimen.numberedtextlist_defaulttextmargin).toFloat()
        textMargin = a.getDimension(R.styleable.NumberedTextList_textMargin, defTextMargin).toInt()

        val defItemMargin =
            resources.getDimensionPixelSize(R.dimen.numberedtextlist_defaultitemmargin).toFloat()
        itemMargin = a.getDimension(R.styleable.NumberedTextList_itemMargin, defItemMargin).toInt()

        val indicatorSeparator = a.getString(R.styleable.NumberedTextList_indicatorSeparator)
        if (!indicatorSeparator.isNullOrBlank()) {
            this.indicatorSeparator = indicatorSeparator
        }

        indicatorType = a.getInt(R.styleable.NumberedTextList_indicatorType, INDICATOR_TYPE_BULLET)

        indicatorColor =
            a.getResourceId(R.styleable.NumberedTextList_indicatorColor, defaultTextColor)

        a.recycle()

        if (isInEditMode) {
            val loremIpsum = resources.getString(R.string.loremipsum_300char)
            setTextList(
                listOf(
                    loremIpsum.subSequence(0, 50),
                    loremIpsum.subSequence(0, 50),
                    loremIpsum.subSequence(0, 50),
                    loremIpsum.subSequence(0, 50)
                )
            )
        }

        setup()
    }

    fun setLabeledTextList(list: List<Pair<CharSequence, CharSequence>>) {
        val resultList = mutableListOf<CharSequence>()
        list.forEach {
            val ssb = SpannableStringBuilder("${it.first}${it.second}")
            val typefaceSpan =
                CompatibleTypefaceSpan(ResourcesCompat.getFont(context, R.font.iranyekan_bold)!!)
            ssb.setSpan(typefaceSpan, 0, it.first.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            resultList.add(ssb)
        }
        textList = resultList
        setup()
    }

    fun setTextList(text: List<CharSequence>) {
        textList = text

        setup()
    }

    fun setTextColorRes(@ColorRes textColor: Int) {
        setTextColor(ContextCompat.getColor(context, textColor))
    }

    fun setTextColor(@ColorInt textColor: Int) {
        this.textColor = textColor

        setup()
    }

    fun setTextSize(@DimenRes textSize: Int) {
        this.textSize = resources.pixelsToSp(resources.getDimensionPixelSize(textSize).toFloat())

        setup()
    }

    fun setIndicatorSeparatorRes(@StringRes indicatorSeparator: Int) {
        setIndicatorSeparator(resources.getString(indicatorSeparator))
    }

    fun setIndicatorSeparator(indicatorSeparator: String) {
        this.indicatorSeparator = indicatorSeparator

        setup()
    }

    fun setIndicatorType(@IndicatorType indicatorType: Int) {
        this.indicatorType = indicatorType

        setup()
    }

    fun setTextMarginRes(@DimenRes textMargin: Int) {
        setTextMargin(resources.getDimensionPixelSize(textMargin))
    }

    fun setTextMargin(textMargin: Int) {
        this.textMargin = textMargin

        setup()
    }

    fun setItemMarginRes(@DimenRes itemMargin: Int) {
        setItemMargin(resources.getDimensionPixelSize(itemMargin))
    }

    fun setItemMargin(itemMargin: Int) {
        this.itemMargin = textMargin

        setup()
    }

    private fun setup() {
        removeAllViews()

        val inflater = LayoutInflater.from(context)

        textList.forEachIndexed { i, text ->
            val itemView =
                inflater.inflate(R.layout.item_numberedtextlist, this, false)

            val textIndicator = itemView.findViewById<TextView>(R.id.text_indicator)
            val textSeparator = itemView.findViewById<TextView>(R.id.text_separator)
            val textText = itemView.findViewById<TextView>(R.id.text_text)

            textText.text = text

            when (indicatorType) {
                INDICATOR_TYPE_NUMBER -> {
                    textIndicator.text = (i + 1).toString().toPersianNumber()
                    textIndicator.setTextColor(indicatorColor)

                    textSeparator.visibility = View.VISIBLE
                    textSeparator.text = indicatorSeparator
                    textSeparator.setTextColor(indicatorColor)
                }
                INDICATOR_TYPE_BULLET -> {
                    textSeparator.visibility = View.GONE
                    val drawable = ContextCompat.getDrawable(context, R.drawable.ic_bullet_6dp)
                    drawable?.mutate()?.let { it ->
                        DrawableCompat.setTint(it, ContextCompat.getColor(context, indicatorColor))
                        textIndicator.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            it,
                            null,
                            null,
                            null
                        )
                    }
                }
            }

            (textText.layoutParams as LinearLayout.LayoutParams).marginStart = textMargin
            (textText.layoutParams as LinearLayout.LayoutParams).topMargin = textMargin
            (textText.layoutParams as LinearLayout.LayoutParams).bottomMargin = textMargin

            textIndicator.setTextColor(textColor)
            textSeparator.setTextColor(textColor)
            textText.setTextColor(textColor)

            textIndicator.textSize = textSize
            textSeparator.textSize = textSize
            textText.textSize = textSize



//            (itemView.layoutParams as LayoutParams).bottomMargin = itemMargin

            addView(itemView)
        }
    }
}

object NumberedTextListBindings {
    @JvmStatic
    @BindingAdapter("textList")
    fun bindTextTapColor(textList: NumberedTextList, list: List<String>?) {
        list?.let { textList.setTextList(it) }
    }
}