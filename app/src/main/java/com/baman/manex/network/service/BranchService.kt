package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.Cacheable
import com.baman.manex.data.dto.*
import com.baman.manex.network.ApiResponse
import com.baman.manex.util.ext.plus
import com.baman.manex.data.model.PageableModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BranchService {

    @POST("partner/branch/getnearbybranchs")
    fun getNearStore(@Body nearByParametersDto: NearByParametersDto): LiveData<ApiResponse<List<StoreInfoDto>>>

    @POST("partner/branch/getbranches")
    fun getStores(@Body requestDto: GetBranchesRequestDto): LiveData<ApiResponse<StoresDTO>>

    @GET("partner/branch/getbranchbytype")
    fun getStoreByType(@Query("type") type: Int) : LiveData<ApiResponse<StoreByTypeDto>>

    @POST("partner/branch/getbranchesofflinecount")
    fun getEarnFilterCount(@Body filterItems: RequestDto): LiveData<ApiResponse<FilterCountResultDto>>

    @GET("partner/branch/getbranchbyid")
    fun getBranchById(@Query("id") id: Long,@Query("source") source:String?): LiveData<ApiResponse<StoreInfoDto>>

    @POST("partner/branch/like")
    fun setBranchLike(@Body branchLikeDto: LikeDto):  LiveData<ApiResponse<Boolean>>

    @POST("partner/branch/getbranchesoffline")
    fun getOfflineBranch(@Body requestDto: RequestDto):  LiveData<ApiResponse<BranchResultDto>>

}

sealed class GetBranchesRequestDto(
    val filterType: FilterType,
    direction: Int,
    lastId: Int,
    val filter: FilterRequestDto?,
    val search: SearchModel?,
    val sortItem: Int? = null
): PageableModel(direction, lastId), Cacheable

class DefaultBranchesRequestDto
    : GetBranchesRequestDto(FilterType.Non, 0, 0, null, null) {
    override fun getCacheToken(): String {
        return "filterType=none"
    }
}

class SearchBranchesRequestDto(
    search: SearchModel
): GetBranchesRequestDto(FilterType.Search, 0, 0, null, search) {
    override fun getCacheToken(): String {
        val builder = StringBuilder()
        builder + "filterType=search"
        if (search?.text.isNullOrBlank())
            builder + "{search.branchType.name}${search?.filterTags}"
        else builder + search?.text
        return builder.toString()
    }
}

class FilterBranchesRequestDto(
    filter: FilterRequestDto,
    sortItem: Int ? = null
): GetBranchesRequestDto(FilterType.Filter, 0, 0, filter, null, sortItem) {
    override fun getCacheToken(): String {
        val builder = StringBuilder()
        builder + "filterType=filter"
        //todo: build a more sophisticated cache token

        return builder.toString()
    }
}

enum class FilterType {
    Non, Filter, Search
}

enum class BranchType {
    Offline, Online
}


class FilterModel(
    val branchType: BranchType,
    val minValue: Long?,
    val maxValue: Long?,
    val canBuy: Boolean?,
    val filterTags: List<String>?
)

class SearchModel(
    val branchType: BranchType,
    val text: String?,
    val filterTags: List<Int>?
)