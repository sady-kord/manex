package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class TransactionFilterOptionsDto (

    @SerializedName("minValue")
    var minManexCount : Int ,

    @SerializedName("maxValue")
    var maxManexCount : Int ,

    @SerializedName("groupTitles")
    var groupTitles : List<String> ,

    @SerializedName("filterTags")
    var types : List<FilterItemsDto>
)