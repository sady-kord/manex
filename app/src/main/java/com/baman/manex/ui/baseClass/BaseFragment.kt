package com.baman.manex.ui.baseClass


import android.graphics.Color
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    var getStatusBarColor: String = ""


    override fun onResume() {
        super.onResume()

        if (getStatusBarColor.isNullOrEmpty()) {
            (activity as BaseActivity).setStatusBarColor(parseColor("0031af"))
        } else {
            (activity as BaseActivity).setStatusBarColor(parseColor(getStatusBarColor))
        }

    }

    fun parseColor(colorCode: String): Int {
        var color = colorCode
        if (!colorCode.contains("#"))
            color = "#$colorCode"

        return Color.parseColor(color)
    }

    protected fun setBaseStatusColor(color: String) {
        getStatusBarColor = color
        (activity as BaseActivity).setStatusBarColor(parseColor(getStatusBarColor))
    }

}