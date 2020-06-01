package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileMenuDto (

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("isEnabled")
    var isEnabled: Boolean,

    @SerializedName("imagePath")
    var imagePath: String,

    @SerializedName("menuItemType")
    var menuItemType: String,

    @SerializedName("order")
    var order: Int,

    @SerializedName("code")
    var code: Int

):Parcelable