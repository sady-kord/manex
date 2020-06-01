package com.baman.manex.di

import com.baman.manex.diframeLayout.LoginFragmentBuilderModule
import com.baman.manex.ui.home.exoVideoPlayer.VideoPlayerActivity
import com.baman.manex.ui.burn.burnManexStore.imageGalleryViewer.ImageGalleryViewerActivity
import com.baman.manex.ui.login.LoginActivity
import com.baman.manex.ui.main.MainActivity
import com.baman.manex.ui.splash.StartupActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): StartupActivity

    @ContributesAndroidInjector(modules = [LoginFragmentBuilderModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [MainFragmentBuilderModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [MainFragmentBuilderModule::class])
    abstract fun contributeImageGalleryViewerActivity(): ImageGalleryViewerActivity

    @ContributesAndroidInjector()
    abstract fun contributeVideoPlayerActivity(): VideoPlayerActivity

}
