package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@TypeConverters(OperatorTypeConverters::class)
data class MessageDto (

//    "key": "string",
//"value": "string",
//"messageType": "Error"

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id : Long = 0,

    @SerializedName("key")
    @ColumnInfo(name = "key")
    var key: String,

    @SerializedName("messageType")
    @ColumnInfo(name = "messageType")
    var messageType: String,

    @SerializedName("value")
    @ColumnInfo(name = "value")
    var value: String


) : Parcelable