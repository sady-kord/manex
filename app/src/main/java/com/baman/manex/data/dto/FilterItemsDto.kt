package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterItemsDto (

    @SerializedName("order")
    var order: Int,

    @SerializedName("key")
    var key : Int ,

    @SerializedName("title")
    var title : String

) : Parcelable