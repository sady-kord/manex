package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RuleDto(
    @SerializedName("value")
    var list: List<String>
) : Parcelable