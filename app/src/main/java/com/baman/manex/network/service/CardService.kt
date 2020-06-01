package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.RegisterCardDto
import com.baman.manex.data.dto.RegisterCardResultDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET

interface CardService {

    @GET("pay/pay/GetRegisterdCards")
    fun getRegisterCards(): LiveData<ApiResponse<RegisterCardResultDto>>
}