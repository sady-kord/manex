package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.FriendDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET

interface FriendService {

    @GET("auth/invitecode/getdata")
    fun getData(): LiveData<ApiResponse<FriendDto>>


}