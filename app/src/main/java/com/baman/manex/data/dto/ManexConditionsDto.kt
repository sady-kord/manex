package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@TypeConverters(OperatorTypeConverters::class)
class ManexConditionsDto(

    @SerializedName("manexCount")
    var manexCount: Int ,

    @SerializedName("price")
    var price: Int ,

    @SerializedName("title")
    var title: String


) : Parcelable