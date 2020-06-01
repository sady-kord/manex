package com.baman.manex.ui.profile.myTransaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.FilterCountResultDto
import com.baman.manex.data.dto.MyTransactionGroupDto
import com.baman.manex.data.dto.TransactionFilterOptionsDto
import com.baman.manex.network.Resource
import com.baman.manex.network.Success
import com.baman.manex.network.repositories.WalletRepository
import com.baman.manex.network.service.FilterTransactionRequestDto
import com.baman.manex.network.service.GetTransactionRequestDto
import javax.inject.Inject

class MyTransactionViewModel @Inject constructor(
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()

    private var _requestDto = MutableLiveData<GetTransactionRequestDto>(
        GetTransactionRequestDto(
            null,
            FilterTransactionRequestDto(mutableListOf(), null, null, mutableListOf())
        )
    )

    val requestDto: LiveData<GetTransactionRequestDto> = _requestDto

    val getTransactions: LiveData<Resource<List<MyTransactionGroupDto>>> =
        Transformations.switchMap(_requestDto) { input ->
            walletRepository.getPostTransaction(input)
        }

    //region filter

    /**
     * Filter fragment logic
     */


    private var minPrice = 0
    private var maxPrice = 0
    private fun createInitialFilter() = GetTransactionRequestDto(
        null,
        FilterTransactionRequestDto(mutableListOf(), minPrice, maxPrice)
    )

    private var setInitialValue = false

    private var _filterRequestDto =
        MutableLiveData<GetTransactionRequestDto>(createInitialFilter())

    val filterRequestDto: LiveData<GetTransactionRequestDto> = _filterRequestDto

    val getTransactionFilterOption: LiveData<Resource<TransactionFilterOptionsDto>> =
        Transformations.map(walletRepository.getTransactionFilterOption()) { res ->
            if (res is Success && !setInitialValue) {
                res.data?.minManexCount?.let { minPrice = it }
                res.data?.maxManexCount?.let { maxPrice = it }

                _filterRequestDto.value = createInitialFilter()

                setInitialValue = true
            }
            res
        }

    val getFilterCount: LiveData<Resource<FilterCountResultDto>> = Transformations
        .switchMap(_filterRequestDto) { input ->
            walletRepository.getFilterCount(input)
        }

    fun refresh() {
        reloadTrigger.value = true
        _requestDto.value = GetTransactionRequestDto(null, null)
    }

    fun setFilterMinValue(minValue: Int) {
        _filterRequestDto.value?.filterModel?.minValue = minValue

        _filterRequestDto.value = _filterRequestDto.value
    }

    fun setFilterMaxValue(maxValue: Int) {
        _filterRequestDto.value?.filterModel?.maxValue = maxValue

        _filterRequestDto.value = _filterRequestDto.value
    }

    fun setFilterTag(tags: List<Int>) {
        _filterRequestDto.value?.filterModel?.filterTags = tags

        _filterRequestDto.value = _filterRequestDto.value
    }

    fun setChipsTag(tag: String) {
        var tags = mutableListOf<String>()
        tags.add(tag)
        _filterRequestDto.value?.filterModel?.groupTitle = tags

        _filterRequestDto.value = _filterRequestDto.value
    }

    fun submitFilter() {
        setFilterDto(_filterRequestDto.value)
    }

    fun resetFilter() {
        clearFilter()
    }

    fun clearFilter() {
        _filterRequestDto.value = createInitialFilter()
        setFilterDto(_filterRequestDto.value)
    }

    fun isInitialFilter(): Boolean {
        if (createInitialFilter().searchText.equals(_filterRequestDto.value?.searchText)) {
                if (createInitialFilter().filterModel?.isEqual(_filterRequestDto.value?.filterModel)!!)
                    return true
        }
        return false
//        return _filterRequestDto.value == createInitialFilter()
    }


    private fun setFilterDto(filter: GetTransactionRequestDto?) {
        _requestDto.value = filter
        _requestDto.value = _requestDto.value
    }

    fun setRequestDtoForWaitingManex(getTransactionRequestDto: GetTransactionRequestDto) {
        _requestDto.value = getTransactionRequestDto
    }

    //endregion

}

