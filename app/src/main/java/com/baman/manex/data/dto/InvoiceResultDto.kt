package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class InvoiceResultDto (

    @SerializedName("totalCount")
    var totalCount : Int ,

    @SerializedName("data")
    var data : List<InvoiceDataDto>
)