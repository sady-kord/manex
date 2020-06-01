package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class LikeDetailDto(

    @SerializedName("value")
    var count : Boolean
)