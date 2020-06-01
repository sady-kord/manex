package com.baman.manex.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.AppExecutors
import com.baman.manex.dao.ValidationDao
import com.baman.manex.data.dto.ChallengeDto
import com.baman.manex.data.dto.PhoneDto
import com.baman.manex.data.dto.ValidationDto
import com.baman.manex.data.dto.ValidationRuleDto
import com.baman.manex.network.repositories.TokenRepository
import com.baman.manex.network.Resource
import javax.inject.Inject

//@OpenForTesting
class LoginViewModel @Inject constructor(
    val appExecutors: AppExecutors,
    val tokenRepository: TokenRepository,
    val validationDao: ValidationDao
) : ViewModel() {


    private val _token = MutableLiveData<String>()
    var token: LiveData<String> = _token

    var phoneLiveData = MutableLiveData<String>()

    var _validationRuleDto: ValidationRuleDto? = null

    lateinit var hashValue: String

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    val phoneValidity: LiveData<Boolean> = Transformations.map(phoneNumber) { value ->
        if (_validationRuleDto != null) {
        //    val pattern = _validationRuleDto!!.regex!!.toRegex()
            var a = "\\A(0098{1}|\\+?98{1}|0{1})?9{1}[0-9]{9}\\z"
            value.matches(a.toRegex())
        } else {
            false
        }
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setToken(token: String) {
        _token.value = token
    }

    fun setPhoneValidationRule(validationRuleDto: ValidationRuleDto) {
        _validationRuleDto = validationRuleDto
    }

    fun setPhoneForGetToken(phone: String) {
        this.phoneLiveData.value = phone
    }

    fun setHash(hash: String) {
        hashValue = hash
    }

    val validation: LiveData<ValidationDto> = validationDao.findValidation()

    var getToken: LiveData<Resource<ChallengeDto>> =
        Transformations
            .switchMap(phoneLiveData) { input ->
                tokenRepository.getToken(PhoneDto(input, hashValue))
            }
}
