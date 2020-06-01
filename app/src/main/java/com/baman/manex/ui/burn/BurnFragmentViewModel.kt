package com.baman.manex.ui.burn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.*
import com.baman.manex.data.model.RequestType
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.PartnerRepository
import com.baman.manex.network.repositories.ShopRepository
import com.baman.manex.network.repositories.VoucherRepository
import com.baman.manex.network.repositories.WalletRepository
import javax.inject.Inject

class BurnFragmentViewModel @Inject
constructor(
    walletRepository: WalletRepository,
    private val partnerRepository: PartnerRepository,
    shopRepository: ShopRepository,
    voucherRepository: VoucherRepository
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()
    private var _requestDto = MutableLiveData<BasicRequest>()
    private var _requestVoucherDto = MutableLiveData<BasicBurnRequest>()

    val shopData: LiveData<Resource<ShopResultDto>> =
        Transformations.switchMap(_requestDto) { input ->
            shopRepository.shopList(input)
        }

    val voucherData: LiveData<Resource<VoucherResultDto>> =
        Transformations.switchMap(_requestVoucherDto) { input ->
            voucherRepository.voucherList(input)
        }

    fun refresh() {
        _requestDto.value = BasicRequest()
    }

    fun refreshVoucher() {
        _requestVoucherDto.value = BasicBurnRequest()
    }

    val walletData: LiveData<Resource<WalletDto>> =
        Transformations.switchMap(reloadTrigger) {
            walletRepository.getUserManex()
        }


    fun bannerData(pageType: RequestType) = partnerRepository.getBanners(pageType)

    fun refreshManexCount() {
        reloadTrigger.value = false
    }
}
