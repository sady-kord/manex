package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.PartnerSearchDto
import com.baman.manex.data.dto.StoreByTypeDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PartnerSearchService {

    @GET("partner/partner/search")
    fun search(@Query("search") search:String) : LiveData<ApiResponse<PartnerSearchDto>>

}