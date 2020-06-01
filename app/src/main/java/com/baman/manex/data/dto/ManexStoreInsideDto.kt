package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(OperatorTypeConverters::class)
data class ManexStoreInsideDto (

    @SerializedName("progressValue")
    var progressValue: Int,

    @SerializedName("manexCount")
    var manexCount: Int,

    @SerializedName("id")
    var id: Long,

    @SerializedName("imageUrl")
    var imageUrl: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("model")
    var model: String,

    @SerializedName("name")
    var name: String

) : Parcelable