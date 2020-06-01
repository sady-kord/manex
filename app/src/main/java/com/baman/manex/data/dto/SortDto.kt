package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class SortDto(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @SerializedName("title")
    var title: String,

    @SerializedName("key")
    var key: Int,

    @SerializedName("order")
    var order: Int,

    @SerializedName("checked")
    var checked: Boolean

) : Parcelable