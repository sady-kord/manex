package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(OperatorTypeConverters::class)
data class WorkTimesDto (

    @SerializedName("id")
    var id : Long,

    @SerializedName("creationTimeUtc")
    var creationTimeUtc : String,

    @SerializedName("status")
    var status : String,


    @SerializedName("title")
    var title : String


) : Parcelable