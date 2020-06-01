package com.baman.manex.ui.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.PartnerSearchDto
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.PartnerSearchRepository
import javax.inject.Inject

class HomeSearchViewModel @Inject constructor(
    private var partnerSearchRepository: PartnerSearchRepository
): ViewModel() {

    var query = MutableLiveData<String>()

    val querySelectionResult: LiveData<Resource<PartnerSearchDto>> = Transformations.switchMap(query) {
        if (it.isNullOrEmpty()) {
            return@switchMap MutableLiveData<Resource<PartnerSearchDto>>()
        }
        partnerSearchRepository.search(it)
    }

    fun setQuery(query: String) {
        this.query.value = query
    }

    fun refresh() {
        query.value  = ""
    }
}