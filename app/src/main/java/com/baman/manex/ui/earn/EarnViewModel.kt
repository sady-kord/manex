package com.baman.manex.ui.earn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.*
import com.baman.manex.data.model.RequestType
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.*
import javax.inject.Inject

class EarnViewModel @Inject
constructor(
    branchRepository: BranchRepository,
    walletRepository: WalletRepository,
    private val partnerRepository: PartnerRepository,
    voucherRepository: VoucherRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()
    private var _requestDto = MutableLiveData<BasicRequest>()
    private var _requestVoucherDto = MutableLiveData<RequestDto>()


    val offlineData: LiveData<Resource<BranchResultDto>> =
        Transformations.switchMap(_requestDto) { input ->
            branchRepository.loadOfflineStore(input)
        }

    val voucherData: LiveData<Resource<VoucherResultDto>> =
        Transformations.switchMap(_requestVoucherDto) { input ->
            voucherRepository.voucherList(input)
        }

    fun refresh() {
        _requestDto.value = BasicRequest()
    }

    fun refreshVoucher() {
        _requestVoucherDto.value = BasicEarnRequest()
    }

    fun setPagingVoucherRequest(page : Int) {
        _requestVoucherDto.value = ShowMoreEarnRequest(page)
    }

    val walletData: LiveData<Resource<WalletDto>> =
        Transformations.switchMap(reloadTrigger) {
            walletRepository.getUserManex()
        }

    fun bannerData(pageType: RequestType) = partnerRepository.getBanners(pageType)

    fun userHasCard() = paymentRepository.userHasCard()

    fun setQuery(text: String) {
        if (text.isNullOrEmpty())
            _requestVoucherDto.value = BasicEarnRequest()
        else
            _requestVoucherDto.value = SearchEarnRequest(text)
    }
    fun refreshManexCount() {
        reloadTrigger.value = false
    }
}
