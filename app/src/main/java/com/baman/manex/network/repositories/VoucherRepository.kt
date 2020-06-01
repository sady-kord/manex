package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.*
import com.baman.manex.network.Resource
import com.baman.manex.network.service.BuyService
import com.baman.manex.network.service.VoucherService
import javax.inject.Inject

class VoucherRepository @Inject constructor(
    private var voucherService: VoucherService,
    private var buyService: BuyService
) {

    fun voucherList(requestDto: RequestDto): LiveData<Resource<VoucherResultDto>> =
        OnlineResource(voucherService.getVouchers(requestDto)).asLiveData()

    fun buyVoucherBurn(id: Long): LiveData<Resource<ResultBuyDto>> =
        OnlineResource(buyService.buyVoucherBurn(ShopBuyRequestDto(id,null,null))).asLiveData()

    fun buyVoucherEarn(id: Long): LiveData<Resource<ResultBuyDto>> =
        OnlineResource(buyService.buyVoucherEarn(ShopBuyRequestDto(id,"Android-app",false))).asLiveData()

    fun voucherCount(requestDto: RequestDto): LiveData<Resource<CountResultDto>> =
        OnlineResource(voucherService.getVoucherCount(requestDto)).asLiveData()

    fun voucherDetail(id : Long): LiveData<Resource<VoucherDetailDto>> =
        OnlineResource(voucherService.getVoucherDetail(GoodRequestDto(id))).asLiveData()

    fun voucherConfirmDialog(id : Long): LiveData<Resource<ConfirmDialogDto>> =
        OnlineResource(voucherService.getVoucherDetail(id)).asLiveData()
}