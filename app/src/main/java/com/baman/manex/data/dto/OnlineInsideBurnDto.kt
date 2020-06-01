package com.baman.manex.data.dto

import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(OperatorTypeConverters::class)
data class OnlineInsideBurnDto (
    @SerializedName("title")
    var title: String,

    @SerializedName("count")
    var count: Int,

    @SerializedName("branches")
    var onlines: List<OnlineStoreInsideDto>
)