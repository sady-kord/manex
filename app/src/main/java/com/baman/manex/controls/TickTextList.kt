package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import com.baman.manex.R
import com.baman.manex.data.dto.BranchSloganDto
import kotlinx.android.synthetic.main.item_ticktext.view.*

class TickTextList: LinearLayoutCompat {

    private lateinit var textList: List<CapabilityText>

    interface CapabilityText {
        fun getText(): CharSequence
        fun getSubtext(): CharSequence
    }

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

        if (isInEditMode) {
            textList = listOf(
                object: CapabilityText {
                    override fun getText() = "عنوان"
                    override fun getSubtext() = "هزینه\u200C سفرتان به طرز چشم\u200Cگیری کاهش می یابد"
                },
                object: CapabilityText {
                    override fun getText() = "عنوان"
                    override fun getSubtext() = "هزینه\u200C سفرتان به طرز چشم\u200Cگیری کاهش می یابد"
                },
                object: CapabilityText {
                    override fun getText() = "عنوان"
                    override fun getSubtext() = "هزینه\u200C سفرتان به طرز چشم\u200Cگیری کاهش می یابد"
                }
            )
            setup()
        }
    }

    fun setTextList(text: List<CapabilityText>) {
        textList = text

        setup()
    }

    private fun setup() {
        removeAllViews()

        val inflater = LayoutInflater.from(context)

        textList.forEach {
            val itemView =
                inflater.inflate(R.layout.item_ticktext, this, false)

            itemView.text_text.text = it.getText()
            itemView.text_subtext.text = it.getSubtext()

            addView(itemView)
        }
    }
}

object Bindings {
    @JvmStatic
    @BindingAdapter("sloganList")
    fun bindTextTapColor(textList: TickTextList, conditionList: List<BranchSloganDto>?) {
        conditionList?.let { textList.setTextList(it) }
    }
}