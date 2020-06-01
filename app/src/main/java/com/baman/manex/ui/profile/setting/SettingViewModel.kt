package com.baman.manex.ui.profile.setting

import androidx.lifecycle.ViewModel
import com.baman.manex.network.repositories.TokenRepository
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    val tokenRepository: TokenRepository
): ViewModel() {

    val logout = tokenRepository.logoutToken()

}