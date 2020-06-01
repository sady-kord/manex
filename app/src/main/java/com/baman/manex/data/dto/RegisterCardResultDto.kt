package com.baman.manex.data.dto

import com.google.gson.annotations.SerializedName

data class RegisterCardResultDto(

    @SerializedName("userHasCard")
    var userHasCard: Boolean,

    @SerializedName("cards")
    var cards: List<RegisterCardDto>

)



