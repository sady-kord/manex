package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateUserDto(
    @SerializedName("value")
    var value: Boolean
)
