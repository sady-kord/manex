package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactsDto(

    @SerializedName("imageUrl")
    var imageUrl : String ,
    @SerializedName("prefix")
    var prefix : String ,
    @SerializedName("value")
    var value : String ,
    @SerializedName("contactType")
    var contactType : String


): Parcelable
