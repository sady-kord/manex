package com.baman.manex.ui.profile.setting.cities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baman.manex.ui.common.Section
import com.baman.manex.network.Resource
import com.baman.manex.network.Success
import javax.inject.Inject

class CitiesViewModel @Inject constructor(

) : ViewModel() {

    private val mockCities = listOf(
        Section(
            "شهر های فعال", listOf(
                CityDto(
                    0,
                    "تهران",
                    true,
                    true
                ),
                CityDto(
                    1,
                    "شیراز",
                    false,
                    true
                )
            )
        ),
        Section(
            "به زودی در این شهر ها", listOf(
                CityDto(
                    2,
                    "تبریز",
                    false,
                    false
                ),
                CityDto(
                    3,
                    "یزد",
                    false,
                    false
                )
            )
        )
    )

    fun getCities() =
        MutableLiveData<Resource<List<Section<CityDto>>>>(
            Success(mockCities,200)
        )

}