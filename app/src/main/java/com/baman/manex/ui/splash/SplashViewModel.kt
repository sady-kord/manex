package com.baman.manex.ui.splash

import androidx.lifecycle.ViewModel
import com.baman.manex.network.repositories.SyncAgent
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    syncAgent: SyncAgent
) : ViewModel() {
    val syncStatus = syncAgent.syncStatus
}
