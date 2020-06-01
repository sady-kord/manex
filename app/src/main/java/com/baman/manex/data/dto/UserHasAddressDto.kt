package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class UserHasAddressDto(
    @SerializedName("value")
    var hasAddress: Boolean
)