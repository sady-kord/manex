package com.baman.manex.network.repositories

import com.baman.manex.network.service.PartnerSearchService
import javax.inject.Inject


class PartnerSearchRepository @Inject constructor(
    private val partnerSearchService: PartnerSearchService
) {

    fun search(query : String ) = OnlineResource(partnerSearchService.search(query)).asLiveData()

}
