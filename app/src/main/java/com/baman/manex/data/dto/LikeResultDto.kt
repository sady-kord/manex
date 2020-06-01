package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LikeResultDto (

    @SerializedName("value")
    val value: Boolean

):Parcelable