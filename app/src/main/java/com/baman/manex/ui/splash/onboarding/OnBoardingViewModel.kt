package com.baman.manex.ui.splash.onboarding

import androidx.lifecycle.ViewModel
import com.baman.manex.network.repositories.OnBoardingRepository
import javax.inject.Inject

class OnBoardingViewModel @Inject constructor(
    onBoardingRepository: OnBoardingRepository
) : ViewModel() {

    val onBoardingSlides = onBoardingRepository.getOnBoardingSlides()
}
