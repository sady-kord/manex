package com.baman.manex.ui.burn.burnVoucher.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.*
import com.baman.manex.data.model.SearchType
import com.baman.manex.network.service.*
import com.baman.manex.network.repositories.FilterRepository
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.VoucherRepository
import javax.inject.Inject

class BurnVoucherSearchViewModel @Inject constructor(
    filterRepository: FilterRepository,
    var voucherRepository: VoucherRepository
) : ViewModel() {

    var isFirstTime = true

    var query = MutableLiveData<String>()

    val searchTags: LiveData<Resource<List<SearchTag>>> =
        filterRepository.loadSearchTags(SearchType.RedeemGiftCardSearch)

    var selectedTagKey = MutableLiveData<SearchTag>()

    private var keyList = mutableListOf<Int>()

    val querySelectionResult: LiveData<Resource<VoucherResultDto>> = Transformations.switchMap(query) {
        if (it.isNullOrEmpty()) {
            return@switchMap MutableLiveData<Resource<VoucherResultDto>>()
        }
        voucherRepository.voucherList(SearchBurnRequest(it))
    }


    val tagSelectionResult: LiveData<Resource<VoucherResultDto>> =
        Transformations.switchMap(selectedTagKey) {
            voucherRepository.voucherList(SearchTagBurnRequest(1,keyList))
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

//    fun refresh() {
//        _searchType.value = SearchType.RedeemGiftCardSearch
//        query.value = ""
//    }
}
