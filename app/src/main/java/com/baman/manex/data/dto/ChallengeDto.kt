package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class ChallengeDto (

    @SerializedName("token")
    var token : String,
    @SerializedName("code")
    var code : String
)