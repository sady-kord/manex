package com.baman.manex.ui.profile.myTransaction.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.MyTransactionGroupDto
import com.baman.manex.network.service.GetTransactionRequestDto
import com.baman.manex.network.repositories.WalletRepository
import com.baman.manex.network.Resource
import javax.inject.Inject

class MyTransactionSearchViewModel @Inject constructor(
    val walletRepository: WalletRepository
) : ViewModel() {

    val query = MutableLiveData<String>("")

    val querySelectionResult: LiveData<Resource<List<MyTransactionGroupDto>>> =
        Transformations.switchMap(query) {
            walletRepository.getPostTransaction(GetTransactionRequestDto(query.value,null))
        }

    fun setQuery(query: String) {
        this.query.value = query
    }
}