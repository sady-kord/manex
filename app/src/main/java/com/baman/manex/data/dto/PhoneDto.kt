package com.baman.manex.data.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PhoneDto(

    @Expose
    @SerializedName("phone")
    private var phone : String ,

    @Expose
    @SerializedName("appSignatureHash")
    private var appSignatureHash : String


)