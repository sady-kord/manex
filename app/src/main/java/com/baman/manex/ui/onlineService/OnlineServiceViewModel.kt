package com.baman.manex.ui.onlineService

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.BurnDto
import com.baman.manex.data.dto.FilterRequestDto
import com.baman.manex.data.dto.StoresDTO
import com.baman.manex.data.dto.WalletDto
import com.baman.manex.network.service.*
import com.baman.manex.network.repositories.BranchRepository
import com.baman.manex.network.repositories.BurnRepository
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.WalletRepository
import javax.inject.Inject

class OnlineServiceViewModel @Inject constructor(
    branchRepository: BranchRepository,
    burnRepository: BurnRepository ,
    walletRepository: WalletRepository
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()

    private var _requestBurnDto = MutableLiveData<GetBurnRequestDto>(DefaultBurnRequestDto(GetType.Online))

    val showMoreOnlineData: LiveData<Resource<StoresDTO>> =
        branchRepository.loadStoresDataWithoutCash(FilterBranchesRequestDto(FilterRequestDto(BranchType.Online,
            0,0,false, mutableListOf())))

    val showMoreOnlineBurnData: LiveData<Resource<BurnDto>> = Transformations.
        switchMap(_requestBurnDto) {input ->
            burnRepository.loadBurnData(input)
        }

    fun refresh() {
        reloadTrigger.value = true
        _requestBurnDto.value = DefaultBurnRequestDto(GetType.Online)
    }

    val walletData: LiveData<Resource<WalletDto>> =
        walletRepository.getUserManex()
}