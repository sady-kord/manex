package com.baman.manex.ui.login

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.baman.manex.R
import com.baman.manex.ui.baseClass.BaseActivity
import com.baman.manex.util.SMSReceiver
import com.google.android.gms.auth.api.phone.SmsRetriever

class LoginActivity : BaseActivity() ,SMSReceiver.OTPReceiveListener {

    var smsReceiver: SMSReceiver? = null

    var TAG = VerifyCodeFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        startSMSListener()

    }

    private fun startSMSListener() {
        try {
            smsReceiver = SMSReceiver()
            smsReceiver!!.setOTPListener(this)
            val intentFilter = IntentFilter()
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
            this.registerReceiver(smsReceiver, intentFilter)
            val client = SmsRetriever.getClient(this)
            val task = client.startSmsRetriever()
            task.addOnSuccessListener {
                Log.d(TAG,"started")
            }
            task.addOnFailureListener {
                Log.d(TAG,"failed")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onOTPReceived(otp: String) {

        onOtpRecieve?.onRecieve(otp)

        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver!!)
        }
    }

    override fun onOTPTimeOut() {

    }

    override fun onOTPReceivedError(error: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver!!)
        }
    }

    override fun onPause() {
        super.onPause()
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver!!)
        }
    }


    protected var onOtpRecieve: OnOtpRecieve? = null

    interface OnOtpRecieve {
        fun onRecieve(otp: String)
    }

    fun setOnOtpRecieveListener(listener: OnOtpRecieve?) {
        onOtpRecieve = listener!!
    }

}
