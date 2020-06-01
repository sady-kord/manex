package com.baman.manex.network

import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import com.baman.manex.App
import com.baman.manex.AppExecutors
import com.baman.manex.BuildConfig
import com.baman.manex.dao.VerifyTokenDao
import com.baman.manex.data.dto.RefreshTokenDto
import com.baman.manex.data.dto.VerifyTokenDto
import com.baman.manex.network.service.TokenService
import com.baman.manex.ui.baseClass.BaseActivity
import com.baman.manex.util.NetworkUtil.isOnline
import com.baman.manex.util.Preferences
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named


class ConnectivityInterceptor @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    val app: App
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response? {

        if (!isOnline(connectivityManager)) {
            throw NoConnectivityException()
        }

        synchronized(this) {

            val originalRequest = chain.request()
            val authenticationRequest = request(originalRequest)
            val initialResponse = chain.proceed(authenticationRequest)

            when {
                initialResponse.code() == 403 || initialResponse.code() == 401 -> {

                    var oAuth = Preferences.getAccessToken(app.baseContext)
                    var refresh = Preferences.getRefreshToken(app.baseContext)

                    val httpClient = OkHttpClient.Builder()
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .readTimeout(2, TimeUnit.MINUTES)
                        .writeTimeout(2, TimeUnit.MINUTES)
                        .addInterceptor { chain ->
                            chain.proceed(request(chain.request()))
                        }
                        .build()

                    val gson = GsonBuilder()
                        .setLenient()
                        .create()


                    val retrofit = Retrofit.Builder()
                        .baseUrl(BuildConfig.BASE_URL_API)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(httpClient)
                        .build()

                    val responseNewTokenLoginModel = retrofit.create(TokenService::class.java).
                        refreshToken(RefreshTokenDto(refresh!!,oAuth!!)).execute()


                    return when {
                        responseNewTokenLoginModel == null || responseNewTokenLoginModel.code() != 200 -> {
                           app.restartAndLogoutApplication(app.baseContext)
                           null
                        }
                        else -> {
                            responseNewTokenLoginModel.body().let {
                                it?.data?.let {
                                    Preferences.setRefreshToken(app.baseContext, it.refreshToken)
                                    Preferences.setAccessToken(app.baseContext,it.accessToken)
                                }
                            }
                            chain.proceed(request(originalRequest))
                        }
                    }
                }
                else -> return initialResponse
            }

        }

    }

    private fun request(originalRequest: Request): Request {
        var oAuth = Preferences.getAccessToken(app.baseContext)

        return originalRequest.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-type", "application/json")
            .addHeader("MNX-Client", "AndroidApp")
            .addHeader("Authorization", "Bearer $oAuth")
            .build()

    }

}
