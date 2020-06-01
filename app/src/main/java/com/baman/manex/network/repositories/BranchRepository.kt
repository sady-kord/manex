package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.LikeDto
import com.baman.manex.data.dto.RequestDto
import com.baman.manex.data.dto.StoresDTO
import com.baman.manex.network.Resource
import com.baman.manex.network.service.BranchService
import com.baman.manex.network.service.GetBranchesRequestDto
import com.baman.manex.network.service.SearchBranchesRequestDto
import javax.inject.Inject


class BranchRepository @Inject constructor(
    private val branchService : BranchService
) {
    fun searchForBranches(searchRequest: SearchBranchesRequestDto) =
        OnlineResource(branchService.getStores(searchRequest)).asLiveData()

    fun loadStoresData(getBranchesRequestDto: GetBranchesRequestDto) =
        OnlineResource(branchService.getStores(getBranchesRequestDto)).asLiveData()

    fun loadOfflineStore(requestDto: RequestDto) =
        OnlineResource(branchService.getOfflineBranch(requestDto)).asLiveData()

    private fun getRoundedLocation(lat: String, lng: String): String {
        return "${round(lat)},${round(lng)}"
    }

    private fun round(lat: String): String {
        return lat.substring(0, 4)
    }

    fun loadStoresDataWithoutCash(getBranchesRequestDto: GetBranchesRequestDto): LiveData<Resource<StoresDTO>> =
        OnlineResource(branchService.getStores(getBranchesRequestDto)).asLiveData()

    fun getBranchById(id: Long,source:String?) =
        OnlineResource(branchService.getBranchById(id,source)).asLiveData()

    fun setBranchLike(branchLikeDto: LikeDto): LiveData<Resource<Boolean>> =
        OnlineResource(branchService.setBranchLike(branchLikeDto)).asLiveData()
}