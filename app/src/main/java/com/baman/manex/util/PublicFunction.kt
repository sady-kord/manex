package com.baman.manex.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.text.Spannable
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.baman.manex.R
import com.baman.manex.util.snack.SnackHelper
import java.util.regex.Pattern


object PublicFunction {

    private const val CLIP_DATA_LABEL = "clipData"

    @JvmStatic
    fun copyToClipBoard(context: Context, text: CharSequence) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(CLIP_DATA_LABEL, text)
        clipboard.setPrimaryClip(clip)
    }

    @JvmStatic
    fun shareText(
        activity: Activity,
        text: CharSequence,
        subject: CharSequence? = null,
        chooserTitle: CharSequence = activity.getString(R.string.share_choosertitle),
        showToast: Boolean = false
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        subject?.let { intent.putExtra(Intent.EXTRA_SUBJECT, subject) }
        intent.putExtra(Intent.EXTRA_TEXT, text)

        val activityInfo = intent.resolveActivityInfo(activity.packageManager, intent.flags)
        if (null != activityInfo && activityInfo.exported) {
            activity.startActivity(Intent.createChooser(intent, chooserTitle))
        } else {
            val message = activity.getString(R.string.invitefrined_share_failuremessage)
            if (showToast) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            } else {
                SnackHelper.showSnack(
                    activity, message
                )
            }
        }
    }

    @JvmStatic
    fun getDrawable(id: Int, context: Context): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getDrawable(id)
        } else {
            context.resources.getDrawable(id)
        }
    }

    @JvmStatic
    fun getColor(id: Int, context: Context): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getColor(id)
        } else {
            context.resources.getColor(id)
        }
    }

    @JvmStatic
    fun handleContactResult(data: Intent, context: Context): String? {

        var phoneNumber: String? = null
        val contactData = data.data

        val cursor1 = context.contentResolver.query(contactData!!, null, null, null, null)
        if (cursor1!!.moveToFirst()) {

            val tempContactID =
                cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID))
            val cursor2 = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + tempContactID,
                null,
                null
            )

            while (cursor2!!.moveToNext()) {
                val tempNumberHolder =
                    cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                if (tempNumberHolder.substring(0, 3) == "+98") {
                    phoneNumber = tempNumberHolder
                } else
                    phoneNumber = tempNumberHolder
            }
        }
        return phoneNumber
    }

    @JvmStatic
    fun convertPixelsToDp(px: Float, context: Context): Float {
        val displayMetrics = context.resources.displayMetrics
        val dp = Math.round(px / (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))
        return dp.toFloat()
    }

    @JvmStatic
    fun convertDpToPixels(dp: Float, context: Context): Int {
        val resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }

    @JvmStatic
    fun shareLinkDialog(context: Context, text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
//            putExtra(Intent.EXTRA_SUBJECT, "hello")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    @JvmStatic
    fun openUrlInBrowser(context: Context,url : String){
        var webUrl = url
        if (!webUrl.startsWith("http://") && !webUrl.startsWith("https://"))
            webUrl = "http://$webUrl"

        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
        context.startActivity(browserIntent)
    }

    @JvmStatic
    fun openBrowserDialog(activity: Activity, link: String) {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        activity.startActivity(browserIntent)
    }

    @JvmStatic
    fun shareLocationDialog(context: Context, latitude: Double, longitude: Double) {
        val uri = ("geo:" + latitude.toString() + ","
                + longitude.toString() + "?q=" + latitude
            .toString() + "," + longitude)
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            )
        )

    }


    @JvmStatic
    fun isValidEmailAddress(email: String): Boolean {
        val ePattern =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()
    }

    @JvmStatic
    fun isValidPostCode(postCode: String): Boolean {
        return postCode.length == 10
    }

    @JvmStatic
    fun addPriceSeparator(price: String): String {
        var a = price.split(" ")

        if (a.size > 1) {
            var priceSeparated = String.format("%,d", a[0].toLong())
            return priceSeparated + " " + a[1]
        } else {
            return String.format("%,d", price.toLong())
        }

    }

    @JvmStatic
    fun increaseFontSizeForPath(
        spannable: Spannable,
        path: String,
        increaseTime: Float
    ): Spannable {
        val startIndexOfPath = spannable.toString().indexOf(path)
        spannable.setSpan(
            RelativeSizeSpan(increaseTime), startIndexOfPath,
            startIndexOfPath + path.length, 0
        )
        return spannable
    }

    @JvmStatic
    fun actionCall(context: Context, phoneNumber: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", phoneNumber, null)
            )
        )
    }

}