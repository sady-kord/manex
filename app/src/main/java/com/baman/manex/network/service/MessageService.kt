package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.MessageDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET

interface MessageService {

    @GET("message/getoperatorconfig")
    fun operatorConfig(): LiveData<ApiResponse<List<Map<String, String>>>>

    @GET("config/message/getmessages")
    fun getMessages(): LiveData<ApiResponse<List<MessageDto>>>

}