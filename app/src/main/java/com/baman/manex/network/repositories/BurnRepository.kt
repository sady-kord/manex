package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.Resource
import com.baman.manex.network.service.BurnService
import com.baman.manex.network.service.GetBurnRequestDto
import javax.inject.Inject


class BurnRepository @Inject constructor(
    private val burnService: BurnService
) {
    fun loadBurnData(getBurnRequestDto: GetBurnRequestDto): LiveData<Resource<BurnDto>> =
        OnlineResource(burnService.burnManex(getBurnRequestDto)).asLiveData()

    fun loadStoreManexInfoWithoutCash(id: Long): LiveData<Resource<StoreManexInfoDto>> =
        OnlineResource(burnService.getShopId(id)).asLiveData()

    fun loadVoucherDetail(voucherDetailId: Long): LiveData<Resource<VoucherDetailDto>> =
        OnlineResource(burnService.getVoucherDetail(voucherDetailId)).asLiveData()

    fun setVoucherLike(voucherLikeDto: LikeDto): LiveData<Resource<LikeDetailDto>> =
        OnlineResource(burnService.setVoucherLike(voucherLikeDto)).asLiveData()

    fun setShopLike(shopLikeDto: LikeDto): LiveData<Resource<Boolean>> =
        OnlineResource(burnService.setShopLike(shopLikeDto)).asLiveData()

    fun getBurnFilterCount(burnFilterRequestDto: BurnFilterRequestDto): LiveData<Resource<FilterCountResultDto>> =
        OnlineResource(burnService.getBurnFilterCount(burnFilterRequestDto)).asLiveData()

}