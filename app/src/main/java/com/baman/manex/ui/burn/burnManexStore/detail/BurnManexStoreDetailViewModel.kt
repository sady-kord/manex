package com.baman.manex.ui.burn.burnManexStore.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.dao.MessageDao
import com.baman.manex.data.dto.ResultGoodBuyDto
import com.baman.manex.data.dto.ShopBuyRequestDto
import com.baman.manex.data.dto.WalletDto
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.*
import javax.inject.Inject

class BurnManexStoreDetailViewModel @Inject constructor(
    val shopRepository: ShopRepository,
    val buyRepository: BuyRepository,
    val likeRepository: LikeRepository,
    private val userRepository: UserRepository,
    val walletRepository: WalletRepository,
    val messageDao: MessageDao
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()

    val errorKey = MutableLiveData<String>()

    fun getStoreDetail(id: Long) = shopRepository.shopDetails(id)

    fun getStoreConfirmDialog(id: Long) = shopRepository.shopConfirmDialog(id)

    fun buyGood(id: Long) :LiveData<Resource<ResultGoodBuyDto>> = buyRepository.buyGood(ShopBuyRequestDto(id,null,null))

    fun setShopLike(id : Long) = likeRepository.setShopLike(id)

    fun getHasAddress() = userRepository.hasAddress()

    val errorMessage =
        Transformations.switchMap(errorKey) {
            messageDao.findMessages()
        }

    fun setErrorKey(key: String) {
        this.errorKey.value = key
    }


    val walletData: LiveData<Resource<WalletDto>> = Transformations
        .switchMap(reloadTrigger) {
            walletRepository.getUserManex()
        }

    fun refresh() {
        reloadTrigger.value = true
    }
}
