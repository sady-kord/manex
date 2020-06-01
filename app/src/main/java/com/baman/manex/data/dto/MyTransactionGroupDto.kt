package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MyTransactionGroupDto(

    @SerializedName("groupTitle")
    var groupTitle: String,
    @SerializedName("groups")
    var userTransaction: List<MyTransactionDto>

)  : Parcelable