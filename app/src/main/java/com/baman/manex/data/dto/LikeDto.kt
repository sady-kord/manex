package com.baman.manex.data.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LikeDto(

    @Expose
    @SerializedName("itemId")
    var itemId : Long
)