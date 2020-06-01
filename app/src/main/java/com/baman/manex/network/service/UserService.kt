package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.UserDto
import com.baman.manex.data.dto.UpdateUserDto
import com.baman.manex.data.dto.UserHasAddressDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {

    @GET("auth/userprofile/get")
    fun getUser(): LiveData<ApiResponse<UserDto>>

    @PUT("auth/userprofile/update")
    fun editUserProfile(@Body userDto: UserDto): LiveData<ApiResponse<UpdateUserDto>>


    @GET("auth/userprofile/hasaddress")
    fun getUserHasAddress(): LiveData<ApiResponse<UserHasAddressDto>>
}
