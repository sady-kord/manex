package com.baman.manex.data.dto

import androidx.lifecycle.MutableLiveData
import com.baman.manex.network.service.GetType
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BurnFilterRequestDto(

    @Expose
    @SerializedName("getEntity")
    var getEntity: GetType? ,

    @Expose
    @SerializedName("filter")
    var filter: RequestDto
)
