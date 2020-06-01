package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.ProfileMenuDto
import com.baman.manex.network.Resource
import com.baman.manex.network.service.MenuService
import javax.inject.Inject


class MenuRepository @Inject constructor(
    private val menuService: MenuService
) {

    fun getMenu(): LiveData<Resource<List<ProfileMenuDto>>> =
        OnlineResource(menuService.getProfileMenu()).asLiveData()

}
