package com.baman.manex.data.dto

import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(OperatorTypeConverters::class)
data class StoresInsideDto(


    @SerializedName("branches")
    var branches: List<OnlineStoreInsideDto>,

    @SerializedName("title")
    var title: String,

    @SerializedName("count")
    var count : Int

)