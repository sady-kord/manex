package com.baman.manex.data.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GuidTokenRequestDto(

    @Expose
    @SerializedName("agent")
    var agent: String,

    @Expose
    @SerializedName("url")
    var url: String,

    @Expose
    @SerializedName("amount")
    var amount: Int,

    @Expose
    @SerializedName("source")
    var source: Int

)