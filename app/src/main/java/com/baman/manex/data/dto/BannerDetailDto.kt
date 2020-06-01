package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class BannerDetailDto (

    @SerializedName("id")
    var id : Int ,

    @SerializedName("imageUrl")
    var imageUrl : String ,

    @SerializedName("imageFileId")
    var imageFileId : Int ,

    @SerializedName("actionUrl")
    var actionUrl : String ,

    @SerializedName("order")
    var order : Int ,

    @SerializedName("bannerId")
    var bannerId :Int
)
