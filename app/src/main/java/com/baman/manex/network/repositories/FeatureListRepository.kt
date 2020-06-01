package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.FilterCountResultDto
import com.baman.manex.data.dto.FilterDto
import com.baman.manex.data.dto.RequestDto
import com.baman.manex.data.dto.SortDto
import com.baman.manex.data.model.PageFilterType
import com.baman.manex.data.model.SortType
import com.baman.manex.network.Resource
import com.baman.manex.network.service.BranchService
import com.baman.manex.network.service.FeatureListService
import javax.inject.Inject


class FeatureListRepository @Inject constructor(
    private val featureListService: FeatureListService,
    private val branchService: BranchService
) {

    fun loadSortListData(sortType: SortType): LiveData<Resource<List<SortDto>>> =
        OnlineResource(featureListService.sortList(sortType.name)).asLiveData()

    fun loadFilterListData(filterType: PageFilterType): LiveData<Resource<FilterDto>> =
        OnlineResource(featureListService.filterList(filterType)).asLiveData()


    fun getEarnFilterCount(filterRequestDto: RequestDto): LiveData<Resource<FilterCountResultDto>> =
        OnlineResource(branchService.getEarnFilterCount(filterRequestDto)).asLiveData()

}