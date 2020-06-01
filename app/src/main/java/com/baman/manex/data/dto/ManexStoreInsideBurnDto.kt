package com.baman.manex.data.dto

import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(OperatorTypeConverters::class)
data class ManexStoreInsideBurnDto (
    @SerializedName("title")
    var title: String,

    @SerializedName("count")
    var count: Int,

    @SerializedName("shops")
    var stores: List<ManexStoreInsideDto>?
)