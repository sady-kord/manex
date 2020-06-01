package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokenInputDto (

    @Expose
    @SerializedName("phone")
    var phone : String ,

    @Expose
    @SerializedName("code")
    var code : String ,

    @Expose
    @SerializedName("token")
    var token : String
) : Parcelable