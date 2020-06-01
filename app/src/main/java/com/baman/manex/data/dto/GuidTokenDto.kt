package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class GuidTokenDto(
    @SerializedName("value")
    var guid: String
)