package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.AppExecutors
import com.baman.manex.data.dto.RegisterCardResultDto
import com.baman.manex.network.service.CardService
import com.baman.manex.network.Resource
import javax.inject.Inject

class CardRepository @Inject constructor(
    private var appExecutors: AppExecutors,
    private var cardService: CardService
) {

    fun getRegisterCards(): LiveData<Resource<RegisterCardResultDto>> =
        OnlineResource(cardService.getRegisterCards()).asLiveData()
}