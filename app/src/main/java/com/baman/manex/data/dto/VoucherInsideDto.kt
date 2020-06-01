package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(OperatorTypeConverters::class)
data class VoucherInsideDto(

    @SerializedName("progressValue")
    var progressValue: Int,

    @SerializedName("id")
    var id: Long,

    @SerializedName("discount")
    var discount: Int,

    @SerializedName("manexCount")
    var manexCount: Int,

    @SerializedName("expier")
    var expiryText: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("subtitle")
    var subTitle: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("imageUrl")
    var imageUrl: String,

    @SerializedName("price")
    var price: String


) : Parcelable