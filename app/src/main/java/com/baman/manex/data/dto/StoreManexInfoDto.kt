package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
@TypeConverters(OperatorTypeConverters::class)
data class StoreManexInfoDto(

    @PrimaryKey
    @SerializedName("id")
    var id: Long,

    @SerializedName("name")
    var name: String,

    @SerializedName("shortName")
    var shortName: String,

    @SerializedName("model")
    var model: String,

    @SerializedName("manexCount")
    var manexCount: Int,

    @SerializedName("description")
    var description: String,

    @SerializedName("imageUrl")
    var imageUrl: String,


    @SerializedName("progressValue")
    var progressValue: Int,


    @SerializedName("shopConditions")
    var shopConditions: List<String>,


    @SerializedName("imageUrls")
    var imageUrls: List<String>,

    @SerializedName("productDetails")
    var productDetails: List<ProductDetailsDto>,

    @SerializedName("likeStatus")
    var likeStatus: Boolean,

    @SerializedName("userCanBuy")
    var userCanBuy: Boolean,

    @SerializedName("manexCountNeed")
    var manexCountNeed: Int

) : Parcelable