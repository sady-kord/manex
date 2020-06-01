package com.baman.manex.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.*

object DeviceStatus {

    fun getNavBarHeight(context : Context) : Int {
        var resources = context!!.resources
        var resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId)
        }
        return 0
    }

    fun hasSoftKeys(windowManager: WindowManager? , context: Context): Boolean {
        var hasSoftwareKeys = true
        hasSoftwareKeys = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val d: Display = windowManager!!.defaultDisplay
            val realDisplayMetrics = DisplayMetrics()
            d.getRealMetrics(realDisplayMetrics)
            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels
            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)
            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels
            realWidth - displayWidth > 0 ||
                    realHeight - displayHeight > 0
        } else {
            val hasMenuKey =
                ViewConfiguration.get(context).hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            !hasMenuKey && !hasBackKey
        }
        return hasSoftwareKeys
    }
}