package com.baman.manex.ui.burn.burnVoucher.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.dao.MessageDao
import com.baman.manex.data.dto.LikeDto
import com.baman.manex.data.dto.WalletDto
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.LikeRepository
import com.baman.manex.network.repositories.VoucherRepository
import com.baman.manex.network.repositories.WalletRepository
import javax.inject.Inject

class BurnVoucherDetailViewModel @Inject constructor(
    val likeRepository: LikeRepository,
    val voucherRepository: VoucherRepository,
    walletRepository: WalletRepository,
    val messageDao: MessageDao
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()

    private val voucheLikeLiveData = MutableLiveData<LikeDto>()

    val errorKey = MutableLiveData<String>()

    //define variable for api call to view observed in activity
    fun getStoreDetail(id: Long) = voucherRepository.voucherDetail(id)

    fun getVoucherDialog(id: Long) = voucherRepository.voucherConfirmDialog(id)

    fun buyVoucher(id: Long) = voucherRepository.buyVoucherBurn(id)

    fun setVoucherLike(id : Long) = likeRepository.setVoucherLike(id)


    val walletData: LiveData<Resource<WalletDto>> = Transformations
        .switchMap(reloadTrigger) {
            walletRepository.getUserManex()
        }

    val errorMessage =
        Transformations.switchMap(errorKey) {
            messageDao.findMessages()
        }

    fun setErrorKey(key: String) {
        this.errorKey.value = key
    }


    fun refresh() {
        reloadTrigger.value = true
    }
}