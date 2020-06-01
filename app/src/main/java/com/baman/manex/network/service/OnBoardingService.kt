package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.OnBoardingSlideDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET

interface OnBoardingService {

    @GET("onbording/getonbordings")
    fun getOnBoardingSlides(): LiveData<ApiResponse<List<OnBoardingSlideDto>>>

}