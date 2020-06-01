package com.baman.manex.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import timber.log.Timber

object Preferences {

    private const val ON_BOARDING_DISPLAYED = "onboarding_diaplayed"
    private const val IS_REGISTER = "IsRegister"
    private const val PHONE_REGISTER = "PhoneRegister"
    private const val MERCHANT_ID = "MerchantID"
    private const val AccessToken = "AccessToken"
    private const val RefreshToken = "RefreshToken"
    private const val Security_Setting_Status = "SecuritySettingStatus"
    private const val User_Has_Card = "UserHasCard"

     fun getSharedPreferences(context: Context): SharedPreferences {
        val PREFS_NAME = "Setting"
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @JvmStatic
    fun setOnBoardingDisplayed(displayed: Boolean, context: Context) {
        getSharedPreferences(context).edit().putBoolean(ON_BOARDING_DISPLAYED, displayed).commit()
    }

    @JvmStatic
    fun getOnBoardingDisplayed(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(ON_BOARDING_DISPLAYED, false)
    }

    @JvmStatic
    fun setRegister(isRegister: Boolean, phoneRegister: String, context: Context) {
        getSharedPreferences(context).edit().putBoolean(IS_REGISTER, isRegister).commit()
        getSharedPreferences(context).edit().putString(PHONE_REGISTER, phoneRegister).commit()
    }

    @JvmStatic
    fun isRegister(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(IS_REGISTER, false)
    }

    @JvmStatic
    fun getPhoneNumber(context: Context): String? {
        return getSharedPreferences(context).getString(PHONE_REGISTER, "")
    }

    @JvmStatic
    fun setMerchantId(context: Context , merchantId : String) {
        getSharedPreferences(context).edit().putString(MERCHANT_ID, merchantId).commit()
    }

    @JvmStatic
    fun getMerchantId(context: Context): String? {
        return getSharedPreferences(context).getString(MERCHANT_ID, "")
    }

    @JvmStatic
    fun setSecuritySettingStatus(isActive: Boolean, context: Context) {
        getSharedPreferences(context).edit().putBoolean(Security_Setting_Status, isActive).commit()
    }

    @JvmStatic
    fun getSecuritySettingStatus(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(Security_Setting_Status, false)
    }

    @JvmStatic
    fun setAccessToken(context: Context, accessToken : String) {
        getSharedPreferences(context).edit().putString(AccessToken, accessToken).commit()
    }

    @JvmStatic
    fun getAccessToken(context: Context): String? {
        return getSharedPreferences(context).getString(AccessToken, "")
    }

    @JvmStatic
    fun setRefreshToken(context: Context, refreshToken : String) {
        getSharedPreferences(context).edit().putString(RefreshToken, refreshToken).commit()
    }

    @JvmStatic
    fun getRefreshToken(context: Context): String? {
        return getSharedPreferences(context).getString(RefreshToken, "")
    }

    @JvmStatic
    fun setUserHasCard(context: Context , userHasCard : Boolean){
        getSharedPreferences(context).edit().putBoolean(User_Has_Card, userHasCard).commit()
    }

    @JvmStatic
    fun getUserHasCard(context: Context) : Boolean{
        return getSharedPreferences(context).getBoolean(User_Has_Card, false)
    }


}