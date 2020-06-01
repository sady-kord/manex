package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShopService {

    @POST("shop/good/list")
    fun getShops(@Body requestDto: RequestDto):  LiveData<ApiResponse<ShopResultDto>>

    @POST("shop/good/count")
    fun getShopCount(@Body requestDto: RequestDto):  LiveData<ApiResponse<CountResultDto>>

    @POST("shop/good/get")
    fun getShopDetail(@Body goodRequestDto: GoodRequestDto):  LiveData<ApiResponse<StoreManexInfoDto>>

    @GET("shop/good/getconfirmdetails")
    fun getShopDialog(@Query("id") id : Long) :  LiveData<ApiResponse<ConfirmDialogDto>>

}