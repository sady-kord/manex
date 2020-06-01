package com.baman.manex.diframeLayout

import com.baman.manex.ui.login.RegisterFragment
import com.baman.manex.ui.login.VerifyCodeFragment
import com.baman.manex.ui.splash.onboarding.OnBoardingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeOnBoardingFragment(): OnBoardingFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragmentFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeVerifyCodeFragmentFragment(): VerifyCodeFragment

}
