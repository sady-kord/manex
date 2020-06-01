package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(OperatorTypeConverters::class)
data class OtherBranchDto(

    @SerializedName("branchType")
    var branchType : String,

    @SerializedName("burnManex")
    var burnManex : Int,

    @SerializedName("burnPrice")
    var burnPrice : Int,

    @SerializedName("longitude")
    var longitude : Double,

    @SerializedName("latitude")
    var latitude : Double,

    @SerializedName("id")
    var id : Long,

    @SerializedName("searchBy")
    var searchBy : String,

    @SerializedName("address")
    var address : String


) : Parcelable
