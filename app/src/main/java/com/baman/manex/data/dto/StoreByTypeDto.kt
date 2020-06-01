package com.baman.manex.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName

@Entity
@TypeConverters(OperatorTypeConverters::class)
data class StoreByTypeDto (

    @PrimaryKey
    @SerializedName("count")
    var count : Int ,

    @SerializedName("list")
    var storeList : List<StoreInfoDto>
)
