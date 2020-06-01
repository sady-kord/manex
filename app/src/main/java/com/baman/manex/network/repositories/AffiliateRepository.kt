package com.baman.manex.network.repositories

import com.baman.manex.network.service.AffiliateService
import javax.inject.Inject


class AffiliateRepository @Inject constructor(
    private val affiliateService: AffiliateService
) {

    fun getGoLink(parentId : Long) = OnlineResource(affiliateService.getGoLink(parentId,"AndroidApp"))
        .asLiveData()
}
