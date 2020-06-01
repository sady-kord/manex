package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyTransactionDto(

    @SerializedName("receiptId")
    var receiptId: Long,
    @SerializedName("transactionDateTime")
    var transactionDateTime: String,
    @SerializedName("totalAmount")
    var totalAmount: String,
    @SerializedName("paidAmount")
    var paidAmount: String,
    @SerializedName("earnManexAmount")
    var earnManexAmount: Int,
    @SerializedName("burnAmount")
    var burnAmount: String,
    @SerializedName("burnManexAmount")
    var burnManexAmount: Int,
    @SerializedName("partnerName")
    var partnerName: String,
    @SerializedName("earnManexDueDate")
    var earnManexDueDate: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("statusDescription")
    var statusDescription: String,
    @SerializedName("type")
    var type: String,

    var isExpanded: Boolean = false

) : Parcelable