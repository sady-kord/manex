package com.baman.manex.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName


@Entity
@TypeConverters(OperatorTypeConverters::class)
data class PartnerSearchDto (

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    @SerializedName("earn")
    var earn : StoresDTO?,

    @SerializedName("burn")
    var burn: BurnDto?

)

