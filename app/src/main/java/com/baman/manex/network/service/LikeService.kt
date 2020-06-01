package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.LikeDto
import com.baman.manex.data.dto.LikeResultDto
import com.baman.manex.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LikeService {

    @POST("auth/like/likebranch")
    fun setBranchLike(@Body branchLikeDto: LikeDto): LiveData<ApiResponse<LikeResultDto>>

    @POST("auth/like/likeproduct")
    fun setShopLike(@Body branchLikeDto: LikeDto): LiveData<ApiResponse<LikeResultDto>>

    @POST("auth/like/likevoucher")
    fun setVoucherLike(@Body branchLikeDto: LikeDto): LiveData<ApiResponse<LikeResultDto>>

}