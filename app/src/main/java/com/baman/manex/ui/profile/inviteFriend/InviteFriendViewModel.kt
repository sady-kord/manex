package com.baman.manex.ui.profile.inviteFriend

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.FriendDto
import com.baman.manex.network.repositories.FriendRepository
import com.baman.manex.network.Resource
import javax.inject.Inject

class InviteFriendViewModel @Inject constructor(
friendRepository: FriendRepository
) : ViewModel() {

    var getData: LiveData<Resource<FriendDto>> =
        friendRepository.getData()

}