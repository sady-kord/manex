package com.baman.manex.ui.earn.earnLocalStore.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.LikeDto
import com.baman.manex.data.dto.WalletDto
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.BranchRepository
import com.baman.manex.network.repositories.LikeRepository
import com.baman.manex.network.repositories.WalletRepository
import javax.inject.Inject

class EarnLocalStoreDetailViewModel @Inject constructor(
    val branchRepository: BranchRepository ,
    val walletRepository: WalletRepository,
    val likeRepository: LikeRepository
): ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()

    fun getBranchDetail(id: Long,source:String?) = branchRepository.getBranchById(id,source)

    fun setBranchLike(id : Long) = likeRepository.setBranchLike(id)

    val walletData: LiveData<Resource<WalletDto>> = Transformations
        .switchMap(reloadTrigger) {
            walletRepository.getUserManex()
        }

    fun refresh(){
        reloadTrigger.value = true
    }

}
