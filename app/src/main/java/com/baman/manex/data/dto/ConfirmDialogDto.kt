package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class ConfirmDialogDto(

    @SerializedName("imageUrl")
    var imageUrl: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("manexCount")
    var manexCount: Int,

    @SerializedName("body")
    var body: String,

    @SerializedName("expireDate")
    var expireDate: String

)