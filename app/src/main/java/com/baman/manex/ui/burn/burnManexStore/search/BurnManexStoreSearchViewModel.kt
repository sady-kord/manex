package com.baman.manex.ui.burn.burnManexStore.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.*
import com.baman.manex.data.model.SearchType
import com.baman.manex.network.service.SearchTag
import com.baman.manex.network.repositories.FilterRepository
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.ShopRepository
import javax.inject.Inject

class BurnManexStoreSearchViewModel @Inject constructor(
    filterRepository: FilterRepository,
    shopRepository: ShopRepository
) : ViewModel() {

    val query = MutableLiveData<String>()

    private var keyList = mutableListOf<Int>()

    val selectedTagKey = MutableLiveData<SearchTag>()

    val searchTags: LiveData<Resource<List<SearchTag>>> =
        filterRepository.loadSearchTags(SearchType.RedeemShopCardSearch)

    val querySelectionResult: LiveData<Resource<ShopResultDto>> = Transformations.switchMap(query) {
        if (it.isNullOrEmpty()) {
            return@switchMap MutableLiveData<Resource<ShopResultDto>>()
        }
        shopRepository.shopList(SearchRequest(it))
    }


    val tagSelectionResult: LiveData<Resource<ShopResultDto>> =
        Transformations.switchMap(selectedTagKey) {
            shopRepository.shopList(SearchTagRequest(1,keyList))
        }

    fun onTagSelected(selectedTagKey: SearchTag) {
        keyList.clear()
        keyList.add(selectedTagKey.key)
        this.selectedTagKey.value = selectedTagKey
    }

    fun setQuery(query: String) {
        if (query.isNullOrBlank()) {
            getLastTagData()
        }else{
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
        query.value = ""
    }
}
