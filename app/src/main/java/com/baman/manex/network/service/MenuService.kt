package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.ProfileMenuDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET

interface MenuService {

    @GET("config/menu/get")
    fun getProfileMenu(): LiveData<ApiResponse<List<ProfileMenuDto>>>

}