package com.baman.manex.data.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshTokenDto(

    @Expose
    @SerializedName("refreshToken")
    var refresh : String ,

    @Expose
    @SerializedName("accessToken")
    var access : String

)