package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@TypeConverters(OperatorTypeConverters::class)
data class FilterDto (

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @SerializedName("minPrice")
    var minPrice : Int ,

    @SerializedName("maxPrice")
    var maxPrice : Int ,

    @SerializedName("filterItems")
    var filterItems : List<FilterItemsDto>

) : Parcelable