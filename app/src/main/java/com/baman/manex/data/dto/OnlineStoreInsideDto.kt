package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@TypeConverters(OperatorTypeConverters::class)
data class OnlineStoreInsideDto(

    @PrimaryKey
    @SerializedName("id")
    var id: Long,

    @SerializedName("branchType")
    var storeType: String?,

    @SerializedName("longitude")
    var longitude: Double?,

    @SerializedName("latitude")
    var latitude: Double?,

    @SerializedName("searchBy")
    var searchBy: String,

    @SerializedName("imagePath")
    var imagePath: String?,

    @SerializedName("address")
    var address: String?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("earnManexConditionTitle")
    var earnManexConditionTitle: ManexConditionsDto?,

    @SerializedName("burnManexConditionTitle")
    var burnManexConditionTitle: ManexConditionsDto?,

    @SerializedName("backgroundColor")
    var backgroundColor: String,

    @SerializedName("statusBarColor")
    var statusColor: String,

    @SerializedName("isBlackTheme")
    var isBlackTheme : Boolean


) : Parcelable