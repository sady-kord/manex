package com.baman.manex.ui.earn.earnLocalStore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.*
import com.baman.manex.data.model.PageFilterType
import com.baman.manex.data.model.SortType
import com.baman.manex.network.Resource
import com.baman.manex.network.Success
import com.baman.manex.network.repositories.BranchRepository
import com.baman.manex.network.repositories.FeatureListRepository
import com.baman.manex.network.repositories.WalletRepository
import javax.inject.Inject

class EarnLocalStoreViewModel  @Inject constructor(
    branchRepository: BranchRepository,
    featureListRepository: FeatureListRepository ,
    walletRepository: WalletRepository
) : ViewModel() {

    var selectedSortData: Int = 0
    var defaultSortData: Int = 0
    private var onBackPress = false

    private var _requestDto = MutableLiveData<RequestDto>()
    val requestDto: LiveData<RequestDto> = _requestDto

    val walletData: LiveData<Resource<WalletDto>> =
        walletRepository.getUserManex()

    val showMoreLocalStoreData: LiveData<Resource<BranchResultDto>> =
        Transformations.switchMap(_requestDto) {
        branchRepository.loadOfflineStore(it)
    }

    val sortData: LiveData<Resource<List<SortDto>>> =
        featureListRepository.loadSortListData(SortType.EarnOfflineSort)

    fun setSortKey(sortKey: Int) {
        _requestDto.value?.sortItem = sortKey
        _requestDto.value?.page = 1
        this.selectedSortData = sortKey
        _requestDto.value = _requestDto.value
    }

    private fun setFilterDto(filter: RequestDto?) {
        _requestDto.value = filter
        _requestDto.value = _requestDto.value
    }

    fun setRequestDto(page: Int, sort: Int){
        if(sort == 0){
            _requestDto.value = ShowMoreRequest(page,null)
        }else {
            _requestDto.value = ShowMoreRequest(page, sort)
        }
    }

    fun refresh() {
        if (_requestDto.value?.sortItem != null && !onBackPress)
            selectedSortData = _requestDto.value?.sortItem!!

        _requestDto.value = ShowMoreRequest(1,selectedSortData)

        _requestDto.value = _filterRequestDto.value

        if (selectedSortData != 0 && !onBackPress)
            _requestDto.value?.sortItem = selectedSortData
    }

    /**
     * Filter fragment logic
     */
    private var minPrice = 0
    private var maxPrice = 0

    private fun createInitialFilter() = InitialFilterRequest(minPrice,maxPrice)

    private var setInitialValue = false

    private var _filterRequestDto = MutableLiveData<RequestDto>(createInitialFilter())
    val filterRequestDto : LiveData<RequestDto> = _filterRequestDto

    val loadFilterData: LiveData<Resource<FilterDto>> = Transformations.map(
        featureListRepository.loadFilterListData(PageFilterType.EarnOffLineFilter)) { res ->
        if (res is Success && !setInitialValue) {
            res.data?.minPrice?.let { minPrice = it }
            res.data?.maxPrice?.let { maxPrice = it }

            _filterRequestDto.value = createInitialFilter()

            setInitialValue = true
        }
        res
    }

    fun setBackPress(status : Boolean){
        onBackPress = status
    }

    val getFilterCount : LiveData<Resource<FilterCountResultDto>> = Transformations
        .switchMap(_filterRequestDto) { input ->
            featureListRepository.getEarnFilterCount(input)
        }

    fun setFilterTags(tags: List<Int>) {
        _filterRequestDto.value?.tags = tags

        _filterRequestDto.value = _filterRequestDto.value
    }

    fun submitFilter() {
        if (selectedSortData != 0)
            _filterRequestDto.value!!.sortItem = selectedSortData
        setFilterDto(_filterRequestDto.value)
    }

    fun resetFilter() {
        clearFilter()
    }

    fun clearFilter() {
        _filterRequestDto.value = createInitialFilter()
        setFilterDto(_filterRequestDto.value)
    }

    fun clearSort() {
        selectedSortData = 0
    }

    fun isInitialFilter(): Boolean {
        return _filterRequestDto.value!!.isEqual(createInitialFilter())
    }

}