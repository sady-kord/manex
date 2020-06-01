package com.baman.manex.ui.onlineService.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.LikeDto
import com.baman.manex.data.dto.WalletDto
import com.baman.manex.network.repositories.AffiliateRepository
import com.baman.manex.network.repositories.BranchRepository
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.WalletRepository
import javax.inject.Inject

class OnlineServiceDetailViewModel @Inject constructor(
    val branchRepository: BranchRepository,
    private val affiliateRepository: AffiliateRepository,
    walletRepository: WalletRepository
): ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()

    private val branchLikeLiveData = MutableLiveData<LikeDto>()

    fun getGoLink(parentId : Long) = affiliateRepository.getGoLink(parentId)

    fun getBranchDetail(id: Long) = branchRepository.getBranchById(id,null)

    val setVoucherLike : LiveData<Resource<Boolean>> = Transformations
        .switchMap(branchLikeLiveData) { input ->
            branchRepository.setBranchLike(input)
        }

    fun setVoucherLikeData(branchLikeDto: LikeDto){
        branchLikeLiveData.value = branchLikeDto
    }

    val walletData: LiveData<Resource<WalletDto>> = Transformations
        .switchMap(reloadTrigger) {
            walletRepository.getUserManex()
        }

    fun refresh(){
        reloadTrigger.value = true
    }
}