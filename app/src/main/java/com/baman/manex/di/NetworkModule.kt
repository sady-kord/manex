package com.baman.manex.di

import android.content.Context
import android.net.ConnectivityManager
import com.baman.manex.App
import com.baman.manex.BuildConfig
import com.baman.manex.network.ConnectivityInterceptor
import com.baman.manex.network.service.*
import com.baman.manex.util.LiveDataCallAdapterFactory
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttp(connectivityInterceptor: ConnectivityInterceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)

        httpClient.addInterceptor(connectivityInterceptor)


        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(app: App) =
        app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    @Named("retrofit_base")
    fun provideRetrofitBase(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("retrofit_pay")
    fun provideRetrofitPay(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_PAY)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("retrofit_test")
    fun provideRetrofitTest(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_TEST)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideMenuService(@Named("retrofit_base") retrofit: Retrofit): MenuService {
        return retrofit.create(MenuService::class.java)
    }

    @Singleton
    @Provides
    fun provideTokenService(@Named("retrofit_base") retrofit: Retrofit): TokenService {
        return retrofit.create(TokenService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(@Named("retrofit_base") retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideBranchService(@Named("retrofit_base") retrofit: Retrofit): BranchService {
        return retrofit.create(BranchService::class.java)
    }

    @Singleton
    @Provides
    fun provideMessageService(@Named("retrofit_base") retrofit: Retrofit): MessageService {
        return retrofit.create(MessageService::class.java)
    }

    @Singleton
    @Provides
    fun provideOnboardingService(@Named("retrofit_base") retrofit: Retrofit): OnBoardingService {
        return retrofit.create(OnBoardingService::class.java)
    }

    @Singleton
    @Provides
    fun provideSortService(@Named("retrofit_base") retrofit: Retrofit?): FeatureListService {
        return retrofit!!.create(FeatureListService::class.java)
    }

    @Singleton
    @Provides
    fun provideBurnService(@Named("retrofit_base") retrofit: Retrofit): BurnService {
        return retrofit.create(BurnService::class.java)
    }

    @Singleton
    @Provides
    fun provideFilterService(@Named("retrofit_base") retrofit: Retrofit?): FilterService {
        return retrofit!!.create(FilterService::class.java)
    }

    @Singleton
    @Provides
    fun provideWalletCoreService(@Named("retrofit_base") retrofit: Retrofit?): WalletCoreService {
        return retrofit!!.create(WalletCoreService::class.java)
    }

    @Singleton
    @Provides
    fun provideFriendService(@Named("retrofit_base") retrofit: Retrofit?): FriendService {
        return retrofit!!.create(FriendService::class.java)
    }

    @Singleton
    @Provides
    fun provideAffiliateService(@Named("retrofit_base") retrofit: Retrofit?): AffiliateService {
        return retrofit!!.create(AffiliateService::class.java)
    }

    @Singleton
    @Provides
    fun provideFileService(@Named("retrofit_base") retrofit: Retrofit?): FileService {
        return retrofit!!.create(FileService::class.java)
    }

    @Singleton
    @Provides
    fun providePaymentService(@Named("retrofit_pay") retrofit: Retrofit?): PaymentService {
        return retrofit!!.create(PaymentService::class.java)
    }

    @Singleton
    @Provides
    fun provideRegisterCardService(@Named("retrofit_pay") retrofit: Retrofit): CardService {
        return retrofit.create(CardService::class.java)
    }

    @Singleton
    @Provides
    fun providePartnerSearchService(@Named("retrofit_base") retrofit: Retrofit): PartnerSearchService{
        return retrofit.create(PartnerSearchService::class.java)
    }

    @Singleton
    @Provides
    fun providePartnerService(@Named("retrofit_base") retrofit: Retrofit): PartnerService {
        return retrofit.create(PartnerService::class.java)
    }

    @Singleton
    @Provides
    fun provideValidationService(@Named("retrofit_base") retrofit: Retrofit): ValidationService{
        return retrofit.create(ValidationService::class.java)
    }

    @Singleton
    @Provides
    fun provideManexSupportService(@Named("retrofit_base") retrofit: Retrofit): ManexSupportService{
        return retrofit.create(ManexSupportService::class.java)
    }

    @Singleton
    @Provides
    fun provideManexShopService(@Named("retrofit_base") retrofit: Retrofit): ShopService{
        return retrofit.create(ShopService::class.java)
    }

    @Singleton
    @Provides
    fun provideManexVoucherService(@Named("retrofit_base") retrofit: Retrofit): VoucherService{
        return retrofit.create(VoucherService::class.java)
    }

    @Singleton
    @Provides
    fun provideManexBuyService(@Named("retrofit_base") retrofit: Retrofit): BuyService{
        return retrofit.create(BuyService::class.java)
    }

    @Singleton
    @Provides
    fun provideLikeService(@Named("retrofit_base") retrofit: Retrofit): LikeService{
        return retrofit.create(LikeService::class.java)
    }

}

