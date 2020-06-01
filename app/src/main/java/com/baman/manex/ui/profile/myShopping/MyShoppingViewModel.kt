package com.baman.manex.ui.profile.myShopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.InvoiceResultDto
import com.baman.manex.data.dto.ShopRequest
import com.baman.manex.data.dto.VoucherRequest
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.BuyRepository
import javax.inject.Inject

class MyShoppingViewModel @Inject constructor(
    val buyRepository: BuyRepository
) : ViewModel() {

    private var _myVoucherDto = MutableLiveData<VoucherRequest>()
    private var _myShopDto = MutableLiveData<ShopRequest>()


    val shopData: LiveData<Resource<InvoiceResultDto>> =
        Transformations.switchMap(_myShopDto) { input ->
            buyRepository.myShopping(input)
        }

    val voucherData: LiveData<Resource<InvoiceResultDto>> =
        Transformations.switchMap(_myVoucherDto) { input ->
            buyRepository.myShopping(input)
        }

    fun setVoucherRequest(page : Int){
        _myVoucherDto.value = VoucherRequest(page)
    }

    fun setShopRequest(page : Int){
        _myShopDto.value = ShopRequest(page)
    }

}