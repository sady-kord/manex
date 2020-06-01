package com.baman.manex.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName

@Entity
@TypeConverters(OperatorTypeConverters::class)
data class StoresDTO(

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    @SerializedName("online")
    var onLine: StoresInsideDto,

    @SerializedName("offline")
    var offline: LocalStoresInsideDto,

    @SerializedName("haveCard")
    var haveCard : Boolean,

    @SerializedName("banners")
    var banners : List<String>?
)

