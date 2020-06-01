package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultGoodBuyDto(

    @SerializedName("hasAddress")
    var hasAddress: Boolean,

    @SerializedName("id")
    var id: String
):Parcelable