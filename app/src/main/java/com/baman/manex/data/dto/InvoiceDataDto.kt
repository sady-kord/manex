package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

open class InvoiceDataDto (

    @SerializedName("title")
    var title : String ,

    @SerializedName("list")
    var list : List<InvoiceDto> ,

    @SerializedName("count")
    var count : Int ,

    @SerializedName("callToAction")
    var callToAction : String
)
