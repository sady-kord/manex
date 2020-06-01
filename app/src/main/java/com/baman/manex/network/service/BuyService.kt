package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.ApiResponse
import com.baman.manex.network.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BuyService {

    @POST("buy/good/burn")
    fun buyGood(@Body goodBuyRequestDto: ShopBuyRequestDto) : LiveData<ApiResponse<ResultGoodBuyDto>>

    @POST("buy/voucher/burn")
    fun buyVoucherBurn(@Body goodBuyRequestDto: ShopBuyRequestDto) : LiveData<ApiResponse<ResultBuyDto>>

    @POST("buy/voucher/earn")
    fun buyVoucherEarn(@Body goodBuyRequestDto: ShopBuyRequestDto) : LiveData<ApiResponse<ResultBuyDto>>

    @GET("buy/Invoice/Get")
    fun getMyPurchaseInfo(@Query("id") id : String) : LiveData<ApiResponse<VoucherDetailDto>>

    @POST("buy/invoice/list")
    fun getMyShoppingResult(@Body buyRequestDto: BuyRequestDto) : LiveData<ApiResponse<InvoiceResultDto>>

}