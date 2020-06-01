package com.baman.manex.ui.profile.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.data.dto.FaqDto
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.ManexSupportRepository
import javax.inject.Inject

class FaqViewModel  @Inject constructor(
    val manexSupportRepository: ManexSupportRepository
) : ViewModel() {

    private val type = MutableLiveData<Int>()

    val faqData: LiveData<Resource<List<FaqDto>>> =
        Transformations.switchMap(type) { input ->
            manexSupportRepository.getFaqData(input)
        }

    fun setType(faqType: Int){
        type.value = faqType
    }

}