package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class SearchByDto (

    @SerializedName("branches")
    var branches : List<StoreInfoDto> ,

    @SerializedName("showMore")
    var showMore : Boolean,

    @SerializedName("count")
    var count : Int
)