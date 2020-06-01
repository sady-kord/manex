package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.Resource
import com.baman.manex.network.service.ShopService
import javax.inject.Inject


class ShopRepository @Inject constructor(
    private var shopService: ShopService
) {

    fun shopList(requestDto: RequestDto): LiveData<Resource<ShopResultDto>> =
        OnlineResource(shopService.getShops(requestDto)).asLiveData()

    fun shopCount(requestDto: RequestDto): LiveData<Resource<CountResultDto>> =
        OnlineResource(shopService.getShopCount(requestDto)).asLiveData()

    fun shopDetails(id : Long): LiveData<Resource<StoreManexInfoDto>> =
        OnlineResource(shopService.getShopDetail(GoodRequestDto(id))).asLiveData()

    fun shopConfirmDialog(id : Long): LiveData<Resource<ConfirmDialogDto>> =
        OnlineResource(shopService.getShopDialog(id)).asLiveData()
}