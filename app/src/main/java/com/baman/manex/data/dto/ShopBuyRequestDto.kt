package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopBuyRequestDto (

    @Expose
    @SerializedName("id")
    var id : Long,

    @Expose
    @SerializedName("UserAgent")
    var UserAgent : String?,

    @Expose
    @SerializedName("useManex")
    var useManex : Boolean?


) : Parcelable