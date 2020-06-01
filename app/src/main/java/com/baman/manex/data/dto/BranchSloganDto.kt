package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.TypeConverters
import com.baman.manex.controls.TickTextList
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@TypeConverters(OperatorTypeConverters::class)
data class BranchSloganDto (

    @SerializedName("title")
    var title : String,

    @SerializedName("subtitle")
    var subtitle : String


) : Parcelable, TickTextList.CapabilityText {
    override fun getText() = title
    override fun getSubtext() = subtitle
}