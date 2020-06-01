package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.AppExecutors
import com.baman.manex.dao.VerifyTokenDao
import com.baman.manex.data.dto.*
import com.baman.manex.network.service.TokenService
import com.baman.manex.util.RateLimiter
import com.baman.manex.network.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private var tokenService: TokenService
) {

    private var repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun getToken(phoneDto: PhoneDto): LiveData<Resource<ChallengeDto>> =
        OnlineResource(tokenService.getToken(phoneDto)).asLiveData()

    fun verifyToken(tokenInputDto: TokenInputDto): LiveData<Resource<VerifyTokenDto>> =
      OnlineResource(tokenService.verifyToken(tokenInputDto)).asLiveData()

    fun logoutToken(): LiveData<Resource<LogoutDto>> =
        OnlineResource(tokenService.logoutToken()).asLiveData()

}