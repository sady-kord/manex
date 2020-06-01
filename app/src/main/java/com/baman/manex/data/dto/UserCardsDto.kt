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
data class UserCardsDto(

    @PrimaryKey
    @SerializedName("id")
    val id : Long ,

    @SerializedName("isManex")
    val isManex : Boolean ,

    @SerializedName("imagePath")
    val imagePath : String ,

    @SerializedName("cardNumber")
    val cardNumber : String
) : Parcelable

