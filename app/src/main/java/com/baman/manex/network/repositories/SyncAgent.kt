package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.baman.manex.App
import com.baman.manex.network.Loading
import com.baman.manex.network.Resource
import com.baman.manex.network.Success
import javax.inject.Inject

class SyncAgent @Inject constructor(
    validationRepository: ValidationRepository,
    onBoardingRepository: OnBoardingRepository,
    messageRepository: MessageRepository
) {

    val syncStatus = MediatorLiveData<SyncResult>()

    init {
        val syncList = mutableListOf<LiveData<out Resource<Any>>>()

        syncList.add(validationRepository.getValidationData())

        syncList.add(messageRepository.getMessages())

//        if (!Preferences.getOnBoardingDisplayed(app)) {
//            syncList.add(onBoardingRepository.getOnBoardingSlides())
//        }

        syncAllAndReport(syncList)
    }

    private fun syncAllAndReport(syncList: List<LiveData<out Resource<Any>>>) {

        if (syncList.isNullOrEmpty()) {
            syncStatus.value = SyncResult.success()
            return
        }

        val syncFlags = mutableListOf<Boolean>()

        syncList.forEachIndexed { i, liveData ->
            syncFlags.add(false)
            syncStatus.addSource(liveData) {
                when (it) {
                    is Loading -> {
                        syncStatus.value = SyncResult.syncing("Started syncing...")
                    }
                    is Success -> {
                        syncFlags[i] = true
                        val type = if (null != it.data) it.data::class.java.canonicalName else "Unknown class"
                        syncStatus.value = SyncResult.syncing("Syncing finished for type: $type")
                    }
                    is Error -> {
                        syncStatus.value = SyncResult.error("Syncing failed with message: " + it.message)
                    }
                }

                var allSync = true
                for (flag in syncFlags) allSync = allSync.and(flag)
                if (allSync) syncStatus.value = SyncResult.success()
            }
        }
    }
}

enum class State {
    SUCCESS,
    ERROR,
    SYNCING
}

data class SyncResult(val status: State, val message: String?) {
    companion object {
        fun success(): SyncResult {
            return SyncResult(State.SUCCESS, null)
        }

        fun error(msg: String): SyncResult {
            return SyncResult(State.ERROR, msg)
        }

        fun syncing(message: String): SyncResult {
            return SyncResult(State.SYNCING, message)
        }
    }
}