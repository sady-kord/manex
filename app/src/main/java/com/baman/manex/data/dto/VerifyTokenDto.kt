package com.baman.manex.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class VerifyTokenDto (

    @PrimaryKey(autoGenerate = true)
    var id : Long ,

    @SerializedName("refreshToken")
    var refreshToken : String ,

    @SerializedName("accessToken")
    var accessToken : String
)
