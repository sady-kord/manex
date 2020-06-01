package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VoucherResultDto(

    @SerializedName("data")
    var data: List<VoucherListDto>,

    @SerializedName("totalCount")
    var totalCount: Int

) : Parcelable