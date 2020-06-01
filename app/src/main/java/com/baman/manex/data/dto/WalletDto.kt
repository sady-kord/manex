package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@TypeConverters(OperatorTypeConverters::class)
@Parcelize
data class WalletDto(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @SerializedName("userId")
    var userId: Long,
    @SerializedName("manexAmount")
    var manexAmount: Double,
    @SerializedName("totalManexAmount")
    var totalManexAmount: Double,
    @SerializedName("pendingManexAmount")
    var pendingManexAmount: Double

) : Parcelable