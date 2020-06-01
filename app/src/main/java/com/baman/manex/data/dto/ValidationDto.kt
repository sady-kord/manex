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
data class ValidationDto(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @SerializedName("phone")
    var phone: ValidationRuleDto?,

    @SerializedName("username")
    var username: ValidationRuleDto?,

    @SerializedName("postalCode")
    var postalcode: ValidationRuleDto?,

    @SerializedName("address")
    var address: ValidationRuleDto?,

    @SerializedName("cellPhone")
    var cellPhone: ValidationRuleDto?,

    @SerializedName("email")
    var email: ValidationRuleDto?

) : Parcelable