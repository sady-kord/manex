package com.baman.manex

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.baman.manex.ui.login.LoginActivity
import com.baman.manex.ui.main.MainActivity
import com.baman.manex.util.Preferences

object Coordinator {

    fun getNextIntent(activity: Activity): Intent {

        getDeepLinkIntent(activity)?.let { return it }

        Log.i("setting" , "coor" + Preferences.isRegister(activity) )
        return if (!Preferences.getOnBoardingDisplayed(activity) || !Preferences.isRegister(activity)) {
            Log.i("setting" , "LoginActivity" )
            Intent(activity, LoginActivity::class.java)

        }else
            Intent(activity, MainActivity::class.java)
    }

    private fun getDeepLinkIntent(activity: Activity): Intent? {
        val intent = activity.intent

        val data = intent.data
        val host = activity.getString(R.string.deep_linking_host)
        val path = activity.getString(R.string.deep_linking_path_add_card)
        val path1 = activity.getString(R.string.deep_linking_path_buy_voucher)
        val path2 = activity.getString(R.string.deep_linking_path_buy_voucher)

        if (data?.path != null && data.host != null
            && host == data.host
            && path == data.path
        ) {
            intent.data = null
            var intent = Intent(activity, MainActivity::class.java).putExtra("path",path)
            return intent

        }


        if (data?.path != null && data.host != null
            && host == data.host
            && path1 == data.path
        ) {
            intent.data = null
            var intent = Intent(activity, MainActivity::class.java).putExtra("path",path1)
            return intent
        }

        if (data?.path != null && data.host != null
            && host == data.host
            && path2 == data.path
        ) {
            intent.data = null
            var intent = Intent(activity, MainActivity::class.java).putExtra("path",path2)
            return intent
        }

        return null
    }
}