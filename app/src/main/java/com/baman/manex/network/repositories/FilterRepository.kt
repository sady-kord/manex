package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.model.SearchType
import com.baman.manex.network.Resource
import com.baman.manex.network.service.FilterService
import com.baman.manex.network.service.SearchTag
import javax.inject.Inject


class FilterRepository @Inject constructor(
    private val filterService: FilterService
) {

    fun loadSearchTags(searchType: SearchType): LiveData<Resource<List<SearchTag>>> =
        OnlineResource(filterService.getSearch(searchType)).asLiveData()
}
