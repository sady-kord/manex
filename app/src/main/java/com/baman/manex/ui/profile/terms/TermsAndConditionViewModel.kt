package com.baman.manex.ui.profile.terms

import androidx.lifecycle.ViewModel
import com.baman.manex.network.repositories.ManexSupportRepository
import com.baman.manex.network.repositories.OnlineResource
import javax.inject.Inject

class TermsAndConditionViewModel @Inject constructor(
    val manexSupportRepository: ManexSupportRepository
) : ViewModel() {

    val getRule = manexSupportRepository.getRuleData()

}