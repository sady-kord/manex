package com.baman.manex.util.snack

import android.app.Activity
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import com.andrognito.flashbar.Flashbar
import com.baman.manex.R
import com.baman.manex.data.dto.VoucherInsideDto
import com.baman.manex.util.CompatibleTypefaceSpan
import com.baman.manex.util.snack.SnackLength.Companion.LENGTH_INDEFINITE
import com.baman.manex.util.snack.SnackLength.Companion.LENGTH_LONG
import com.baman.manex.util.snack.SnackLength.Companion.LENGTH_SHORT


object SnackHelper {

    private val DEFAULT_GRAVITY = Flashbar.Gravity.BOTTOM
    private const val DEFAULT_LENGTH = LENGTH_LONG

    private const val DEFAULT_BACKGROUND_COLOR_RES = R.color.snack_backgroundcolor_default
    private const val DEFAULT_MESSAGE_COLOR_RES = R.color.snack_messagecolor_default
    private const val DEFAULT_TITLE_COLOR_RES = R.color.snack_titlecolor_default
    private const val DEFAULT_ACTIONTITLE_COLOR_RES = R.color.snack_actionbutton_color

    private var builderDialog: Flashbar? = null
    private var builder: Flashbar.Builder? = null
    @JvmStatic
    fun snack(activity: Activity, message: String) {
        showSnack(activity, message)
    }

    var snacTitle: String? = ""
    @JvmStatic
    fun snack(activity: Activity, message: String, @SnackLength length: Long) {
        showSnack(activity, message, null, length)
    }

    @JvmStatic
    fun showSnackWithButton(
        activity: Activity,
        message: String,
        primaryActionText: String,
        marginBottom: Float,
        clickCallback: (() -> Unit),
        title: String? = null,
        @SnackLength length: Long = DEFAULT_LENGTH,
        messageColor: Int = getColor(activity, DEFAULT_MESSAGE_COLOR_RES),
        titleColor: Int = getColor(activity, DEFAULT_TITLE_COLOR_RES),
        actionTitleColor: Int = getColor(activity, DEFAULT_ACTIONTITLE_COLOR_RES)
    ): Flashbar {

        val typeface = ResourcesCompat.getFont(activity, R.font.iranyekan_bold)
        val typefaceMsg = ResourcesCompat.getFont(activity, R.font.iranyekan)

        builder = Flashbar.Builder(activity)
            .gravity(DEFAULT_GRAVITY)
            .backgroundDrawable(R.drawable.background_snack)
            .messageColor(messageColor)
            .titleColor(titleColor)
            .message(message)
            .messageTypeface(typefaceMsg!!)
            .messageSizeInSp(12f)
            .marginBotton(marginBottom)
            .enableSwipeToDismiss()
            .dismissOnTapOutside()
            .primaryActionText(primaryActionText)
            .primaryActionTextColor(actionTitleColor)
            .primaryActionTextTypeface(typeface!!)
            .primaryActionTextSizeInSp(14f)
            .primaryActionTapListener(object : Flashbar.OnActionTapListener {
                override fun onActionTapped(bar: Flashbar) {
                    Log.d("Snackhelper", "onActionClick")
                    clickCallback.invoke()
                }

            })
            .barDismissListener(object : Flashbar.OnBarDismissListener {
                override fun onDismissing(bar: Flashbar, isSwiped: Boolean) {
                }

                override fun onDismissProgress(bar: Flashbar, progress: Float) {
                }

                override fun onDismissed(bar: Flashbar, event: Flashbar.DismissEvent) {
                    snacTitle = ""
                }
            })

        if (length != LENGTH_INDEFINITE) {
            builder?.duration(length)
        }

        if (null != title) {
            val titleSpan = wrapTypeFaceSpan(title, activity)
            builder?.title(titleSpan)
        }

        var a = builder?.build()

        a?.show()

        return a!!

    }

    @JvmStatic
    fun showSnack(
        activity: Activity,
        message: String,
        marginBottom: Float? = 60f,
        @SnackLength length: Long = DEFAULT_LENGTH,
        title: String? = null,
        messageColor: Int = getColor(activity, DEFAULT_MESSAGE_COLOR_RES),
        titleColor: Int = getColor(activity, DEFAULT_TITLE_COLOR_RES)
    ): Flashbar? {
        val messageSpan = wrapTypeFaceSpan(message, activity)

        if (snacTitle == title)
            return null
        snacTitle = title
        if (builderDialog != null) {
            if (builderDialog?.isShowing()!!)
                builderDialog?.dismiss()
        }
        builder = Flashbar.Builder(activity)
            .gravity(DEFAULT_GRAVITY)
            .backgroundDrawable(R.drawable.background_snack)
            .messageColor(messageColor)
            .marginBotton(marginBottom!!)
            .titleColor(titleColor)
            .message(messageSpan)
            .barDismissListener(object : Flashbar.OnBarDismissListener {
                override fun onDismissing(bar: Flashbar, isSwiped: Boolean) {
                }

                override fun onDismissProgress(bar: Flashbar, progress: Float) {
                }

                override fun onDismissed(bar: Flashbar, event: Flashbar.DismissEvent) {
                    snacTitle = ""
                }
            })

        if (length != LENGTH_INDEFINITE) {
            builder?.duration(length)
        }

        if (null != title) {
            val titleSpan = wrapTypeFaceSpan(title, activity)
            builder?.title(titleSpan)
        }

        builderDialog = builder?.build()
        builderDialog?.show()

        return builderDialog
    }

    fun dismiss() {
        if (builderDialog != null) {
            builderDialog?.dismiss()
        }
    }

    private fun wrapTypeFaceSpan(text: String, context: Context): Spannable {
        val typeface = ResourcesCompat.getFont(context, R.font.iranyekan)
        val span = CompatibleTypefaceSpan(typeface!!)
        val ssb = SpannableStringBuilder(text)
        ssb.setSpan(span, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ssb
    }

}