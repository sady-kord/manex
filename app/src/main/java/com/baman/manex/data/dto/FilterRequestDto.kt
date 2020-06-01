package com.baman.manex.data.dto

import android.os.Parcelable
import com.baman.manex.network.service.BranchType
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterRequestDto(

    @Expose
    @SerializedName("branchType")
    var branchType: BranchType?,

    @Expose
    @SerializedName("minValue")
    var minValue: Int?,

    @Expose
    @SerializedName("maxValue")
    var maxValue: Int?,

    @Expose
    @SerializedName("canBuy")
    var canBuy: Boolean,

    @Expose
    @SerializedName("filterTags")
    var filterTags: List<Int> = mutableListOf()
): Parcelable
{
    constructor() : this(null,null, null , false ,  mutableListOf<Int>())
}
