package com.andrognito.flashbar.view

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.andrognito.flashbar.R
import com.andrognito.flashbar.view.ShadowView.ShadowType.BOTTOM
import com.andrognito.flashbar.view.ShadowView.ShadowType.TOP

internal class ShadowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    internal fun applyShadow(type: ShadowType) {
        when (type) {
            TOP -> setShadow(R.drawable.shadow_top)
            BOTTOM -> setShadow(R.drawable.shadow_bottom)
        }
    }

    private fun setShadow(@DrawableRes id: Int) {
        background = ContextCompat.getDrawable(context, id)
    }

    enum class ShadowType {
        TOP, BOTTOM
    }
}