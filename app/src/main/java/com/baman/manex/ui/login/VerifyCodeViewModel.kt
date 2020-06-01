package com.baman.manex.ui.login

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.AppExecutors
import com.baman.manex.data.dto.ChallengeDto
import com.baman.manex.data.dto.PhoneDto
import com.baman.manex.data.dto.TokenInputDto
import com.baman.manex.data.dto.VerifyTokenDto
import com.baman.manex.network.repositories.TokenRepository
import com.baman.manex.network.Resource
import java.util.*
import javax.inject.Inject

class VerifyCodeViewModel @Inject constructor(
    val appExecutors: AppExecutors,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    companion object {
        private const val TIMER_LENGTH: Long = 80000
    }

    private lateinit var hashValue : String

    private var _timerVisibility = MutableLiveData<Boolean>()

    private var _resendText = MutableLiveData<Boolean>()
    var resendText: LiveData<Boolean> = _resendText

    private var mCountDownTimer: CountDownTimer? = null
    private var _timeLeft = MutableLiveData<String>()
    var timeLeft: LiveData<String> = _timeLeft

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    private val tokenInputLiveData = MutableLiveData<TokenInputDto>()

    var phoneLiveData = MutableLiveData<String>()

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setTimer() {
        _timerVisibility.value = true
        mCountDownTimer = object : CountDownTimer(TIMER_LENGTH, 1000) {
            override fun onTick(millisUntilFinish: Long) {
                updateCountDownText(millisUntilFinish)
                _resendText.value = false
            }

            override fun onFinish() {
                _resendText.value = true
            }
        }.start()

    }

    fun updateCountDownText(millisUntilFinish: Long) {
        val seconds = (millisUntilFinish / 1000) % 80

        _timeLeft.value = String.format(Locale.getDefault(), "%02d", seconds)
    }

    fun stopTimer(){
        mCountDownTimer?.cancel()
    }

    fun setVerifyInput(tokenInputDto: TokenInputDto) {
        tokenInputLiveData.value = tokenInputDto
    }

    fun getVerufyInput() = tokenInputLiveData.value

    val getVerifyToken: LiveData<Resource<VerifyTokenDto>> = Transformations
        .switchMap(tokenInputLiveData) { input ->
            tokenRepository.verifyToken(input)
        }

    fun setPhoneForGetToken(phone: String) {
        this.phoneLiveData.value = phone
    }

    fun setHash(hash : String){
        hashValue =hash
    }

    var getToken: LiveData<Resource<ChallengeDto>> =
        Transformations
            .switchMap(phoneLiveData) { input ->
                tokenRepository.getToken(PhoneDto(input,hashValue))
            }
}