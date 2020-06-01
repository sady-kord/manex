package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TokenService {

    @POST("auth/Token/Challenge")
    fun getToken(@Body phoneDto: PhoneDto): LiveData<ApiResponse<ChallengeDto>>

    @POST("auth/Token/Verify")
    fun verifyToken(@Body tokenInputDto: TokenInputDto): LiveData<ApiResponse<VerifyTokenDto>>


    @POST("auth/Token/Refresh")
    fun refreshToken(@Body refreshTokenDto: RefreshTokenDto): Call<ResultDto<VerifyTokenDto>>


    @GET("auth/token/logout")
    fun logoutToken(): LiveData<ApiResponse<LogoutDto>>


}