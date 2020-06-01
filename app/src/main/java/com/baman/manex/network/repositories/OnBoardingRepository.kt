package com.baman.manex.network.repositories

import com.baman.manex.network.service.OnBoardingService
import javax.inject.Inject


class OnBoardingRepository @Inject constructor(
    private val onBoardingService: OnBoardingService
) {

    fun getOnBoardingSlides() = OnlineResource(onBoardingService.getOnBoardingSlides()).asLiveData()

}
