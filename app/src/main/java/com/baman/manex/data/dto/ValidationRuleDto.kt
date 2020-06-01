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
data class ValidationRuleDto(

//    "required": "true",
//"length": "11",
//"startwith": "09",
//"name": "شماره تلفن"

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @SerializedName("required")
    var required: Boolean?,

    @SerializedName("length")
    var length: Int?,

    @SerializedName("minLength")
    var minLength: Int?,

    @SerializedName("maxLength")
    var maxLength: Int?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("regex")
    var regex: String?

) : Parcelable