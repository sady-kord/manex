package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.AppExecutors
import com.baman.manex.dao.ValidationDao
import com.baman.manex.data.dto.ValidationDto
import com.baman.manex.network.ApiResponse
import com.baman.manex.network.Resource
import com.baman.manex.network.service.ValidationService
import javax.inject.Inject

class ValidationRepository @Inject constructor(
    private val validationService: ValidationService,
    val validationDao: ValidationDao,
    val appExecutors: AppExecutors
) {

    fun getValidationData(): LiveData<Resource<ValidationDto>> =
        object : NetworkBoundResource<ValidationDto, ValidationDto>(appExecutors) {
            override fun saveCallResult(item: ValidationDto) {
                validationDao.insertValidation(item)
            }

            override fun shouldFetch(data: ValidationDto?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<ValidationDto> {
                return validationDao.findValidation()
            }

            override fun createCall(): LiveData<ApiResponse<ValidationDto>> {
                return validationService.getValidation()
            }

        }.asLiveData()

}


