package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BranchListDto(

    @SerializedName("title")
    var title: String,

    @SerializedName("objectType")
    var objectType: String,

    @SerializedName("list")
    var branches: List<StoreInfoDto>,

    @SerializedName("commingSoon")
    var commingSoon: Boolean,

    @SerializedName("showMore")
    var showMore: Boolean,

    @SerializedName("count")
    var count: Int

):Parcelable