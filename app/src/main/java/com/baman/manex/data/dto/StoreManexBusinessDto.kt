package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreManexBusinessDto  (
    @SerializedName("manexCount")
    var manexCount : Int ,

    @SerializedName("amount")
    var amount : Long ,

    @SerializedName("text")
    var text : String
) : Parcelable