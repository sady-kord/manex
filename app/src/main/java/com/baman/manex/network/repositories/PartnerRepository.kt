package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.BannerDto
import com.baman.manex.data.model.RequestType
import com.baman.manex.network.Resource
import com.baman.manex.network.service.PartnerService
import javax.inject.Inject

class PartnerRepository @Inject constructor(
    private val partnerService: PartnerService
) {

    fun getBanners(page : RequestType): LiveData<Resource<List<BannerDto>>> =
        OnlineResource(partnerService.getBanner(page)).asLiveData()
}