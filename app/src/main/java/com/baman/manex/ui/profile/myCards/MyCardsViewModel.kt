package com.baman.manex.ui.profile.myCards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.RegisterCardResultDto
import com.baman.manex.network.repositories.CardRepository
import com.baman.manex.network.Resource
import javax.inject.Inject

class MyCardsViewModel @Inject constructor(
    cardRepository: CardRepository
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()

    var getCards: LiveData<Resource<RegisterCardResultDto>> =
        Transformations.switchMap(reloadTrigger) {
            cardRepository.getRegisterCards()
        }

    fun refresh() {
        reloadTrigger.value = true
    }
}