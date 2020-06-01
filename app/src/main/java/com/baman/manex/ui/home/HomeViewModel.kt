package com.baman.manex.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.BannerDto
import com.baman.manex.data.dto.WalletDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.PartnerRepository
import com.baman.manex.network.repositories.PaymentRepository
import com.baman.manex.network.repositories.WalletRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
    , val walletRepository: WalletRepository ,
    partnerRepository: PartnerRepository
) : ViewModel() {

    fun userHasCard() = paymentRepository.userHasCard()

    val walletData: LiveData<Resource<WalletDto>> =
        walletRepository.getUserManex()

    val bannerData: LiveData<Resource<List<BannerDto>>> =
        partnerRepository.getBanners(RequestType.Home)

}
