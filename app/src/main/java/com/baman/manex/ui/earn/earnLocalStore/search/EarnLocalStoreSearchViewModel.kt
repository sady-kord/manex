package com.baman.manex.ui.earn.earnLocalStore.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.*
import com.baman.manex.data.dto.BranchResultDto
import com.baman.manex.data.dto.SearchRequest
import com.baman.manex.data.dto.SearchTagRequest
import com.baman.manex.data.model.SearchType
import com.baman.manex.network.service.SearchTag
import com.baman.manex.network.repositories.BranchRepository
import com.baman.manex.network.repositories.FilterRepository
import com.baman.manex.network.Resource
import javax.inject.Inject

class EarnLocalStoreSearchViewModel @Inject constructor(
    filterRepo: FilterRepository,
    branchRepository: BranchRepository
) : ViewModel() {

    val query = MutableLiveData<String>()

    val moreRequest = MutableLiveData<RequestDto>()

    private var keyList = mutableListOf<Int>()

    var selectedTagKey = MutableLiveData<SearchTag>()

    val searchTags: LiveData<Resource<List<SearchTag>>> =
        filterRepo.loadSearchTags(SearchType.EarnOfflineSearch)

    val querySelectionResult: LiveData<Resource<BranchResultDto>> = Transformations.switchMap(query) {
        if (it.isNullOrEmpty()) {
            return@switchMap MutableLiveData<Resource<BranchResultDto>>()
        }
        branchRepository.loadOfflineStore(SearchRequest(it))
    }


    val moreSelectionResult: LiveData<Resource<BranchResultDto>> =
        Transformations.switchMap(moreRequest) {
            branchRepository.loadOfflineStore(it)
        }

    val tagSelectionResult: LiveData<Resource<BranchResultDto>> =
        Transformations.switchMap(selectedTagKey) {
            branchRepository.loadOfflineStore(SearchTagRequest(1, keyList))
        }

    fun onTagSelected(selectedTagKey: SearchTag) {
        keyList.clear()
        keyList.add(selectedTagKey.key)
        this.selectedTagKey.value = selectedTagKey
    }

    fun setMoreRequest(page : Int,name: Boolean, address: Boolean) {
        this.moreRequest.value = MoreSearchRequest(query.value!!,page, name, address)
    }

    fun setQuery(query: String) {
        if (query.isNullOrBlank()) {
            getLastTagData()
        } else {
            selectedTagKey.value = null
            keyList.clear()
        }
        this.query.value = query
    }

    fun getLastTagData() {
        selectedTagKey.value.let {
            if (it != null)
                onTagSelected(selectedTagKey.value!!)
        }
    }

    fun refresh() {
//        _searchType.value = SearchType.EarnOfflineSearch
        query.value = ""
    }
}
