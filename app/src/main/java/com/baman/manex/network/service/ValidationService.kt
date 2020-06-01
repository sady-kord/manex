package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.UserDto
import com.baman.manex.data.dto.ValidationDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET

interface ValidationService {

    @GET("config/validation/getvalidation")
    fun getValidation(): LiveData<ApiResponse<ValidationDto>>

}