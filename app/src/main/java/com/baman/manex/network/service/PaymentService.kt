package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.GuidTokenDto
import com.baman.manex.data.dto.GuidTokenRequestDto
import com.baman.manex.data.dto.UserHasCardDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface  PaymentService {

    @POST("pay/pay/gettoken")
    fun getTokenGuid(@Body guidTokenRequestDto: GuidTokenRequestDto): LiveData<ApiResponse<GuidTokenDto>>

    @GET("pay/pay/userhascard")
    fun userHasCard(): LiveData<ApiResponse<UserHasCardDto>>

}