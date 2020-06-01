package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@TypeConverters(OperatorTypeConverters::class)
data class ProductDetailsDto(

    @SerializedName("title")
    var title: String,

    @SerializedName("value")
    var value: String?,

    @SerializedName("unit")
    var unit: String?

) : Parcelable