package com.baman.manex.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName

@Entity
@TypeConverters(OperatorTypeConverters::class)
data class BurnDto(

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    @SerializedName("banners")
    var banners : List<String>,

    @SerializedName("online")
    var onLine: OnlineInsideBurnDto,


    @SerializedName("vocher")
    var vocher: VoucherInsideBurnDto,


    @SerializedName("shop")
    var manexStore: ManexStoreInsideBurnDto?

)
