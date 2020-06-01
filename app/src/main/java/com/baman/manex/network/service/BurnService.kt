package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.ApiResponse
import com.baman.manex.data.model.PageableModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BurnService{

    @POST("partner/burn/getburns")
    fun burnManex(@Body getBurnRequestDto: GetBurnRequestDto):  LiveData<ApiResponse<BurnDto>>


    @GET("partner/burn/getshopbyid")
    fun getShopId(@Query("id") id: Long):  LiveData<ApiResponse<StoreManexInfoDto>>

    @GET("partner/burn/getvocherbyid")
    fun getVoucherDetail(@Query("vocherId") vocherId : Long) : LiveData<ApiResponse<VoucherDetailDto>>

    @POST("partner/burn/vocherlike")
    fun setVoucherLike(@Body voucherLikeDto: LikeDto):  LiveData<ApiResponse<LikeDetailDto>>

    @POST("partner/burn/shoplike")
    fun setShopLike(@Body shopLikeDto: LikeDto):  LiveData<ApiResponse<Boolean>>

    @POST("partner/burn/getfiltercount")
    fun getBurnFilterCount(@Body burnFilterRequestDto: BurnFilterRequestDto) : LiveData<ApiResponse<FilterCountResultDto>>
}

sealed class GetBurnRequestDto(
    val getEntity: GetType,
    var filterType: FilterType?,
    direction: Int,
    lastId: Int,
    var filter: FilterRequestDto?,
    val search: BurnSearchModel?,
    var sortItem: Int?
): PageableModel(direction, lastId)


class DefaultBurnRequestDto(
    getType: GetType,
    sortItem: Int? = null
): GetBurnRequestDto(getType,FilterType.Non, 0, 0, null, null, sortItem)

class SearchBurnRequestDto(
    getType: GetType,
    search: BurnSearchModel,
    sortItem: Int? = null
): GetBurnRequestDto(getType,FilterType.Search, 0, 0, null, search, sortItem)

class FilterBurnRequestDto(
    getType: GetType,
    filter: FilterRequestDto? ,
    sortItem: Int? = null
): GetBurnRequestDto(getType,FilterType.Filter, 0, 0, filter, null, sortItem)

enum class GetType{
    All, Online,Vocher,Shop ,Offline
}

class BurnSearchModel(
    val text: String?,
    val filterTags: List<Int>?
)