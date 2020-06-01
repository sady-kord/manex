package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.AppExecutors
import com.baman.manex.dao.UserDao
import com.baman.manex.data.dto.UpdateUserDto
import com.baman.manex.data.dto.UserDto
import com.baman.manex.data.dto.UserHasAddressDto
import com.baman.manex.network.Resource
import com.baman.manex.network.service.UserService
import javax.inject.Inject


//@OpenForTesting
class UserRepository @Inject constructor(
    private var appExecutors: AppExecutors,
    private var userService: UserService,
    private var userDao: UserDao
) {

    fun getUserInfo(): LiveData<Resource<UserDto>> =
        OnlineResource(userService.getUser()).asLiveData()

    fun hasAddress():LiveData<Resource<UserHasAddressDto>> =
        OnlineResource(userService.getUserHasAddress()).asLiveData()

    fun updateUser(userDto: UserDto): LiveData<Resource<UpdateUserDto>> =
        OnlineResource(userService.editUserProfile(userDto)).asLiveData()

}