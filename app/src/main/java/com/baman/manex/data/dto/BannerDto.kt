package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class BannerDto (

    @SerializedName("id")
    var id : Long ,

    @SerializedName("name")
    var name : String ,

    @SerializedName("bannerPage")
    var bannerPage : String ,

    @SerializedName("status")
    var status : Boolean ,

    @SerializedName("bannerDetails")
    var bannerDetails : List<BannerDetailDto>
)
