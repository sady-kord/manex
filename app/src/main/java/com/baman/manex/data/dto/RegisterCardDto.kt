package com.baman.manex.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RegisterCardDto(

    @PrimaryKey
    @SerializedName("iD")
    var id: Long,

    @SerializedName("cardPin")
    var cardPin: String,

    @SerializedName("imageUrl")
    var imageUrl: String,

    @SerializedName("isSamanBank")
    var isSamanBank: Boolean,

    @SerializedName("title")
    var title: String
)
