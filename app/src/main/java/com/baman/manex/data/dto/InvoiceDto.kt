package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class InvoiceDto (

    @SerializedName("id")
    var id : Long ,

    @SerializedName("tranDocId")
    var tranDocId : Long ,

    @SerializedName("manexCount")
    var manexCount : Long ,

    @SerializedName("invoiceStatus")
    var invoiceStatus : String ,

    @SerializedName("status")
    var status : String ,

    @SerializedName("productId")
    var productId : Long ,

    @SerializedName("subTitle")
    var subTitle : String ,

    @SerializedName("productName")
    var productName : String ,

    @SerializedName("imageUrl")
    var imageUrl : String,

    @SerializedName("invoiceType")
    var invoiceType : String

)
