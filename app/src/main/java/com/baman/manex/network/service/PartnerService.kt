package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.BannerDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PartnerService {

    @GET("partner/banner/GetAllBanners")
    fun getBanner(@Query("page") page:RequestType) : LiveData<ApiResponse<List<BannerDto>>>
}