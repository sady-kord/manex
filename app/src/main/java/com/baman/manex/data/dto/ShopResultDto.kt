package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopResultDto(

    @SerializedName("data")
    var data : List<ShopListDto>,

    @SerializedName("totalCount")
    var totalCount: Int

): Parcelable
