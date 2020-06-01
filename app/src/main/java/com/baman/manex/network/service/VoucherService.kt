package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VoucherService {

    @POST("voucher/voucher/list")
    fun getVouchers(@Body requestDto: RequestDto): LiveData<ApiResponse<VoucherResultDto>>

    @POST("voucher/voucher/count")
    fun getVoucherCount(@Body requestDto: RequestDto): LiveData<ApiResponse<CountResultDto>>

    @POST("voucher/voucher/get")
    fun getVoucherDetail(@Body goodRequestDto: GoodRequestDto): LiveData<ApiResponse<VoucherDetailDto>>

    @GET("voucher/voucher/GetConfirmDetails")
    fun getVoucherDetail(@Query("id") id :Long): LiveData<ApiResponse<ConfirmDialogDto>>

}