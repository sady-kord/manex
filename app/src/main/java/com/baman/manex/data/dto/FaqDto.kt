package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FaqDto(

//    "id": 0,
//    "title": "string",
//"description": "string",
//"collapse": true,
//"order": 0,
//"faqType": 1

    @SerializedName("id")
    var id: Long,

    @SerializedName("title")
    var title: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("collapse")
    var collapse: Boolean,

    @SerializedName("order")
    var order: Int,

    @SerializedName("faqType")
    var faqType: String


) : Parcelable