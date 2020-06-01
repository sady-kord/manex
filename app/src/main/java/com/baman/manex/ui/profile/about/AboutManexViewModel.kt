package com.baman.manex.ui.profile.about

import androidx.lifecycle.ViewModel
import com.baman.manex.network.repositories.ManexSupportRepository
import javax.inject.Inject

class AboutManexViewModel @Inject constructor(
    manexSupportRepository: ManexSupportRepository
) : ViewModel() {

    val getAbout = manexSupportRepository.getAboutData()

}