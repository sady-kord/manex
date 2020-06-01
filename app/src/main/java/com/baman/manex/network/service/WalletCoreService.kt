package com.baman.manex.network.service

import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.FilterCountResultDto
import com.baman.manex.data.dto.MyTransactionGroupDto
import com.baman.manex.data.dto.TransactionFilterOptionsDto
import com.baman.manex.data.dto.WalletDto
import com.baman.manex.network.ApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WalletCoreService {

    @GET("wallet-core/account/getusermanex")
    fun getManex(): LiveData<ApiResponse<WalletDto>>

    @GET("wallet-core/Transaction/GetUserTransactions")
    fun getMyTransaction(): LiveData<ApiResponse<List<MyTransactionGroupDto>>>

    @POST("wallet-core/Transaction/GetUserTransactions")
    fun getMyTransactionPost(@Body getTransactionRequestDto: GetTransactionRequestDto): LiveData<ApiResponse<List<MyTransactionGroupDto>>>

    @GET("wallet-core/Transaction/GetFilterOptions")
    fun getTransactionFilterOption() : LiveData<ApiResponse<TransactionFilterOptionsDto>>

    @POST("wallet-core/Transaction/GetUserTransactionsCount")
    fun getFilterCount(@Body filterItems: GetTransactionRequestDto): LiveData<ApiResponse<FilterCountResultDto>>
}


@Parcelize
data class FilterTransactionRequestDto(

    @Expose
    @SerializedName("filterTags")
    var filterTags: List<Int>? = mutableListOf(),

    @Expose
    @SerializedName("minValue")
    var minValue: Int? = 0,

    @Expose
    @SerializedName("maxValue")
    var maxValue: Int? = 0,

    @Expose
    @SerializedName("groupTitles")
    var groupTitle: List<String>? = mutableListOf()

): Parcelable{
    fun isEqual(requestDto: FilterTransactionRequestDto?): Boolean {



        if (minValue != null){
            if (requestDto!!.minValue == null){
                return false
            }else{
                if (minValue!! != requestDto.minValue) {
                    return false
                }
            }
        }else{
            if (requestDto!!.minValue != null){
                return false
            }
        }

        if (maxValue != null){
            if (requestDto.maxValue == null){
                return false
            }else{
                if (maxValue!! != requestDto.maxValue) {
                    return false
                }
            }
        }else{
            if (requestDto!!.maxValue != null){
                return false
            }
        }

        if (filterTags != null){
            if (requestDto.filterTags == null){
                return false
            }else{
                if (filterTags!! != requestDto.filterTags) {
                    return false
                }
            }
        }else{
            if (requestDto!!.filterTags != null){
                return false
            }
        }

        if (groupTitle != null){
            if (requestDto.groupTitle == null){
                return false
            }else{
                if (groupTitle!! != requestDto.groupTitle) {
                    return false
                }
            }
        }else{
            if (requestDto!!.groupTitle != null){
                return false
            }
        }

        return true
    }
}


 class GetTransactionRequestDto(
    val searchText: String?,
    var filterModel: FilterTransactionRequestDto?
)


