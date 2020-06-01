package com.baman.manex.network

import android.app.Activity
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.baman.manex.data.model.InternetError
import com.baman.manex.ui.baseClass.BaseActivity
import com.baman.manex.util.DeviceStatus
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.snack.SnackHelper
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.tuyenmonkey.mkloader.MKLoader


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
sealed class Resource<out T> {
    fun handle(
        fragment: Fragment,
        activity: Activity?,
        recyclerViewSkeletonScreen: RecyclerViewSkeletonScreen? = null,
        viewSkeletonScreen: ViewSkeletonScreen? = null,
        viewSkeletonScreenSecondary : ViewSkeletonScreen? = null ,
        text: AppCompatTextView? = null,
        loading: MKLoader? = null,
        failureCallback: ((String,Int?) -> Unit)? = null,
        callback: ((T,code:Int?) -> Unit)?
    ) {
        when (this) {
            is Loading -> {

                text?.visibility = View.GONE
                loading?.visibility = View.VISIBLE

                data?.let { callback?.invoke(it,null) }

            }
            is Failure -> {
                failureCallback?.invoke(message,code)

                text?.visibility = View.VISIBLE
                loading?.visibility = View.GONE
                viewSkeletonScreen?.hide()
                viewSkeletonScreenSecondary?.hide()
                recyclerViewSkeletonScreen?.hide()

                if (fragment is InternetConnection) {
                    if (message == InternetError.NoInternet.value) {
                        (fragment as InternetConnection).showNoConnection()
                    }
                } else {

                    var height = 60f
                    if (DeviceStatus.hasSoftKeys(activity!!.windowManager, activity))
                        height = DeviceStatus.getNavBarHeight(activity).toFloat()

                    activity.let {
                        if (!message.isNullOrEmpty()) {
                            if (message == InternetError.NoInternet.value)
                                SnackHelper.showSnack(activity, "عدم اتصال به اینترنت", height)
                            else
                                SnackHelper.showSnack(activity, message, height)
                        } else {
                            //SnackHelper.showSnack(activity, "بروز خطا در سرور", height)
                        }
                    }
                }

            }
            is Success -> {
//                (activity as? BaseActivity)?.hideLoading()
                text?.visibility = View.VISIBLE
                loading?.visibility = View.GONE

                recyclerViewSkeletonScreen?.hide()
                viewSkeletonScreen?.hide()
                viewSkeletonScreenSecondary?.hide()
                data?.let { callback?.invoke(it,code) }
            }
        }
    }
}

class Success<T>(val data: T?,val code: Int?) : Resource<T>()
class Failure<T>(val message: String, val data: T?,val code:Int?) : Resource<T>()
class Loading<T>(val data: T?) : Resource<T>()