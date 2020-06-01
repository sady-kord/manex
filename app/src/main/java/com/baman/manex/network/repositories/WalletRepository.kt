package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.AppExecutors
import com.baman.manex.dao.WalletDao
import com.baman.manex.data.dto.FilterCountResultDto
import com.baman.manex.data.dto.MyTransactionGroupDto
import com.baman.manex.data.dto.TransactionFilterOptionsDto
import com.baman.manex.data.dto.WalletDto
import com.baman.manex.network.service.GetTransactionRequestDto
import com.baman.manex.network.service.WalletCoreService
import com.baman.manex.network.Resource
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private var appExecutors: AppExecutors,
    private var walletCoreService: WalletCoreService,
    private var walletDao: WalletDao
) {

    fun getUserManex() : LiveData<Resource<WalletDto>> =
        OnlineResource(walletCoreService.getManex()).asLiveData()

    fun getMyTransaction(): LiveData<Resource<List<MyTransactionGroupDto>>> =
        OnlineResource(walletCoreService.getMyTransaction()).asLiveData()


    fun getPostTransaction(getTransactionRequestDto: GetTransactionRequestDto): LiveData<Resource<List<MyTransactionGroupDto>>> =
        OnlineResource(walletCoreService.getMyTransactionPost(getTransactionRequestDto)).asLiveData()

    fun getTransactionFilterOption(): LiveData<Resource<TransactionFilterOptionsDto>> =
        OnlineResource(walletCoreService.getTransactionFilterOption()).asLiveData()

    fun getFilterCount(filterRequestDto: GetTransactionRequestDto): LiveData<Resource<FilterCountResultDto>> =
        OnlineResource(walletCoreService.getFilterCount(filterRequestDto)).asLiveData()
}