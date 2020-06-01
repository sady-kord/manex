package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.GuidTokenRequestDto
import com.baman.manex.data.dto.UserHasCardDto
import com.baman.manex.network.Resource
import com.baman.manex.network.service.PaymentService
import javax.inject.Inject


class PaymentRepository @Inject constructor(
    private val paymentService: PaymentService
) {

    fun getToken(guidTokenRequestDto: GuidTokenRequestDto) =
        OnlineResource(paymentService.getTokenGuid(guidTokenRequestDto))
            .asLiveData()

    fun userHasCard(): LiveData<Resource<UserHasCardDto>> =
        OnlineResource(paymentService.userHasCard()).asLiveData()

}
