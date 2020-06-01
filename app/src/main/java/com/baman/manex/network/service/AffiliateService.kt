package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.AffiliateGoDto
import com.baman.manex.data.dto.UserHasCardDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AffiliateService {

    @GET("affiliate/Affiliate/Go")
    fun getGoLink(@Query("partnerId") partnerId:Long
                  , @Query("campaign") campaign:String ): LiveData<ApiResponse<AffiliateGoDto>>

}