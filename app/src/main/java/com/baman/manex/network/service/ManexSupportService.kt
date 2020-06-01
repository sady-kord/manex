package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ManexSupportService {

    @GET("config/manexsupport/getfaqs")
    fun getFaqData(@Query("type") faqType: Int): LiveData<ApiResponse<List<FaqDto>>>

    @GET("config/manexsupport/getruleclient")
    fun getRule(): LiveData<ApiResponse<RuleDto>>

    @GET("config/manexsupport/getaboutemanex")
    fun getAbout(): LiveData<ApiResponse<AboutDto>>

    @GET("config/manexsupport/getsupportcontact")
    fun getSupportContact(): LiveData<ApiResponse<SupportContactDto>>

}