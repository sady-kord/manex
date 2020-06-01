package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.FilterDto
import com.baman.manex.data.dto.SortDto
import com.baman.manex.data.model.PageFilterType
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FeatureListService {

    @GET("config/filter/getsort")
    fun sortList(@Query("pageName") pageTitle: String): LiveData<ApiResponse<List<SortDto>>>

    @GET("config/filter/getfilter")
    fun filterList(@Query("pageName") pageName: PageFilterType) : LiveData<ApiResponse<FilterDto>>


}
