package com.baman.manex.ui.profile.addCard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.GuidTokenDto
import com.baman.manex.data.dto.GuidTokenRequestDto
import com.baman.manex.data.model.TokenGuidType
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.PaymentRepository
import javax.inject.Inject

class AddCardViewModel @Inject constructor(
    paymentRepository: PaymentRepository
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()

    val getToken: LiveData<Resource<GuidTokenDto>> =
        Transformations.switchMap(reloadTrigger) {
            paymentRepository.getToken(GuidTokenRequestDto("android",
                "payment-addcard",0,TokenGuidType.RegisteredCard.code))
        }


    fun refresh() {
        reloadTrigger.value = true
    }
}