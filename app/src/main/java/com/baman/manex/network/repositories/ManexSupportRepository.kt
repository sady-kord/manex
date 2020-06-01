package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.AboutDto
import com.baman.manex.data.dto.FaqDto
import com.baman.manex.data.dto.RuleDto
import com.baman.manex.data.dto.SupportContactDto
import com.baman.manex.network.Resource
import com.baman.manex.network.service.ManexSupportService
import javax.inject.Inject


class ManexSupportRepository  @Inject constructor(
    private val manexSupportService: ManexSupportService
) {

    fun getFaqData(type : Int): LiveData<Resource<List<FaqDto>>> =
        OnlineResource(manexSupportService.getFaqData(type)).asLiveData()

    fun getRuleData(): LiveData<Resource<RuleDto>> =
        OnlineResource(manexSupportService.getRule()).asLiveData()

    fun getAboutData(): LiveData<Resource<AboutDto>> =
        OnlineResource(manexSupportService.getAbout()).asLiveData()

    fun getSupportContactData(): LiveData<Resource<SupportContactDto>> =
        OnlineResource(manexSupportService.getSupportContact()).asLiveData()
}
