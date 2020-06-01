package com.baman.manex.ui.burn.burnVoucher.purchase

import androidx.lifecycle.ViewModel
import com.baman.manex.network.repositories.BuyRepository
import javax.inject.Inject

class PurchaseVoucherDetailViewModel @Inject constructor(
    val buyRepository: BuyRepository
) : ViewModel() {

    fun purchaseDetail(id : String) = buyRepository.MyPurchaseInfo(id)
}