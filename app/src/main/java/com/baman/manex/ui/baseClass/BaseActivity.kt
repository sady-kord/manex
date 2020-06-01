package com.baman.manex.ui.baseClass

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.baman.manex.controls.LoadingControl
import com.baman.manex.util.LocaleHelper
import com.master.permissionhelper.PermissionHelper
import dagger.android.support.DaggerAppCompatActivity


@SuppressLint("Registered")
open class BaseActivity : DaggerAppCompatActivity() {

    private var permissionHelper: PermissionHelper? = null

    var loadingControl: LoadingControl? = null

    fun getPermissionHelper(permissions: Array<String>, requestCode: Int): PermissionHelper {
        permissionHelper = PermissionHelper(this, permissions, requestCode)
        return permissionHelper as PermissionHelper
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        LocaleHelper.onAttach(newBase)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.setLocal(baseContext, LocaleHelper.getLanguageSetting(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.window.decorView.layoutDirection =
                if (LocaleHelper.isRTL()) {
                    View.LAYOUT_DIRECTION_RTL
                } else {
                    View.LAYOUT_DIRECTION_LTR
                }
        }
    }

    protected fun setStatusBarColorResource(@ColorRes color: Int) {
        setStatusBarColor(ContextCompat.getColor(this, color))
    }

    fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionHelper != null) {
            permissionHelper!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onStart() {
        super.onStart()
        //region progressDialog init
        if (loadingControl == null)
            loadingControl = LoadingControl(this@BaseActivity)
        //endregion
    }

    override fun onDestroy() {
        super.onDestroy()
        //region progress dialog Dismiss
        if (loadingControl != null)
            loadingControl!!.dismiss()
        //endregion
    }

    fun showLoading() {
        if (loadingControl == null)
            loadingControl = LoadingControl(this@BaseActivity)
        loadingControl!!.show()
    }

    fun hideLoading() {
        loadingControl?.dismiss()
    }

    @Suppress("DEPRECATION")
    open fun hasNetConnection(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val mobileInfo =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val mobileConnected = mobileInfo.state == NetworkInfo.State.CONNECTED
        val wifiInfo =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val wifiConnected = wifiInfo.state == NetworkInfo.State.CONNECTED

        return mobileConnected || wifiConnected
    }

}
