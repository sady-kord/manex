package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterRequestTransactionDto(

    @Expose
    @SerializedName("minValue")
    var minValue: Int? = 0,

    @Expose
    @SerializedName("maxValue")
    var maxValue: Int? = 0,

    @Expose
    @SerializedName("groupTitles")
    var groupTitles: String? = "",

    @Expose
    @SerializedName("filterTags")
    var filterTags: List<Int> = mutableListOf()
): Parcelable
{
    constructor() : this(null,null, null , mutableListOf<Int>())
}



