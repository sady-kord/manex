package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.model.SearchType
import com.baman.manex.network.ApiResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface FilterService {

    @GET("config/filter/getSearch")
    fun getSearch(@Query("pageName") searchType: SearchType): LiveData<ApiResponse<List<SearchTag>>>

}

data class SearchTag(
    @SerializedName("order")
    val order: Int,
    @SerializedName("title")
    val title: String?,
    @SerializedName("key")
    val key: Int
)