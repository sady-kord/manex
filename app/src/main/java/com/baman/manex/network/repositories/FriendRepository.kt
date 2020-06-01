package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.FriendDto
import com.baman.manex.network.Resource
import com.baman.manex.network.service.FriendService
import javax.inject.Inject


class FriendRepository  @Inject constructor(
    private val friendService: FriendService
) {

    fun getData(): LiveData<Resource<FriendDto>> =
        OnlineResource(friendService.getData()).asLiveData()
}
