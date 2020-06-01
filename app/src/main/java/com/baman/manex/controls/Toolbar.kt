package com.baman.manex.controls

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.baman.manex.R
import com.baman.manex.util.glide.GlideApp
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.toolbar.view.*

class Toolbar : ConstraintLayout {

    private var defaultIconWidth = 0

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
        inflater.inflate(R.layout.toolbar, this, true)

        defaultIconWidth = resources.getDimensionPixelOffset(R.dimen.toolbar_height)

        val elevation = resources.getDimensionPixelSize(R.dimen.toolbar_elevation).toFloat()
        ViewCompat.setElevation(this, elevation)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setElevation(elevation)
//        }

        if (isInEditMode) {
            showUpIcon(true, null)
            setTitle("عنوان صفحه")
        }

        image_firsticon.visibility = View.GONE
        image_secondicon.visibility = View.GONE
    }

    fun showUpIcon(show: Boolean, clickListener: ((View) -> Unit)?) {
        if (show) {
            setUpIcon(ContextCompat.getDrawable(context, R.drawable.ic_back_white), clickListener)
        } else {
            setUpIcon(null, null)
        }
    }

    fun setUpIcon(source: Drawable?, clickListener: ((View) -> Unit)?) {
        if (null == source) {
            image_up.visibility = View.GONE
        } else {
            image_up.visibility = View.VISIBLE
            image_up.setImageDrawable(source)
            image_up.setOnClickListener(clickListener)
        }
    }

    fun setUpIconFromUrl(imageUrl: String, isCircle: Boolean) {
        val padding = resources.getDimensionPixelSize(R.dimen.toolbar_up_padding)
        image_up.setPadding(padding, padding, padding, padding)

        if (!imageUrl.isNullOrEmpty())
            if (imageUrl.isEmpty()) {
                image_up.visibility = View.GONE
            } else {
                image_up.visibility = View.VISIBLE

                if (isCircle)
                    GlideApp.with(context!!).load(imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(image_up)
                else
                    GlideApp.with(context!!).load(imageUrl)
                        .into(image_up)
            }
    }

    fun setFirstIcon(@DrawableRes source: Int, clickListener: ((View) -> Unit)?) {
        if (0 == source) {
            setFirstIcon(null, null)
        } else {
            setFirstIcon(ContextCompat.getDrawable(context, source), clickListener)
        }
    }

    fun setFirstIcon(
        source: Drawable?,
        clickListener: ((View) -> Unit)?,
        withIntrinsicBound: Boolean = false
    ) {
        if (null == source) {
            image_firsticon.visibility = View.GONE
        } else {
            image_firsticon.visibility = View.VISIBLE
            image_firsticon.setImageDrawable(source)
            if (withIntrinsicBound) {
                image_firsticon.layoutParams.width = LayoutParams.WRAP_CONTENT
            } else {
                image_firsticon.layoutParams.width = defaultIconWidth
            }

            image_firsticon.setOnClickListener(clickListener)
        }
    }

    fun setFirstIconVisibility(status: Boolean) {
        if (status)
            image_firsticon.visibility = View.VISIBLE
        else
            image_firsticon.visibility = View.GONE
    }

    fun setSecondIcon(@DrawableRes source: Int, clickListener: ((View) -> Unit)?) {
        if (0 == source) {
            setSecondIcon(null, null)
        } else {
            setSecondIcon(ContextCompat.getDrawable(context, source), clickListener)
        }
    }

    fun setSecondIcon(
        source: Drawable?,
        clickListener: ((View) -> Unit)?,
        withIntrinsicBound: Boolean = false
    ) {
        if (null == source) {
            image_secondicon.visibility = View.GONE
        } else {
            image_secondicon.visibility = View.VISIBLE
            image_secondicon.setImageDrawable(source)
            if (withIntrinsicBound) {
                image_firsticon.layoutParams.width = LayoutParams.WRAP_CONTENT
            } else {
                image_firsticon.layoutParams.width = defaultIconWidth
            }

            image_secondicon.setOnClickListener(clickListener)
        }
    }

    fun setTitle(title: CharSequence) {
        if (!title.isEmpty())
            text_title.text = title
    }

    fun setFongroundTintResource(@ColorRes tint: Int) {
        setForgroundTint(ContextCompat.getColor(context, tint))
    }

    fun setForgroundTint(@ColorInt tint: Int) {
        text_title.setTextColor(tint)
        image_up.setColorFilter(tint)
        image_firsticon.setColorFilter(tint)
        image_secondicon.setColorFilter(tint)
    }

    fun getTitle() = text_title.text

    fun getTitleView() = text_title

    fun getUpButton() = image_up

    fun getFirstIcon() = image_firsticon

    fun getSecondIcon() = image_secondicon
}