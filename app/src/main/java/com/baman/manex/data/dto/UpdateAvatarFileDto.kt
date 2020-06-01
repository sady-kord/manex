package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateAvatarFileDto(
    @SerializedName("value")
    var value: String
)