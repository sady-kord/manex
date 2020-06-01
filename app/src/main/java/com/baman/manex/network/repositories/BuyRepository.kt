package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.Resource
import com.baman.manex.network.service.BuyService
import javax.inject.Inject


class BuyRepository @Inject constructor(
    private var buyService: BuyService
) {

    fun buyGood(shopBuyRequestDto: ShopBuyRequestDto): LiveData<Resource<ResultGoodBuyDto>> =
        OnlineResource(buyService.buyGood(shopBuyRequestDto)).asLiveData()

    fun MyPurchaseInfo(id : String) : LiveData<Resource<VoucherDetailDto>> =
        OnlineResource(buyService.getMyPurchaseInfo(id)).asLiveData()

    fun myShopping(buyRequestDto: BuyRequestDto) : LiveData<Resource<InvoiceResultDto>> =
        OnlineResource(buyService.getMyShoppingResult(buyRequestDto)).asLiveData()

}