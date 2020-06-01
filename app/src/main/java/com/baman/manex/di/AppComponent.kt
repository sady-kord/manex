package com.baman.manex.di

import com.baman.manex.App
import com.baman.manex.util.glide.GlideModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuildersModule::class
    ]
)

interface AppComponent {
    fun inject(app: App)
    fun inject(glideModule: GlideModule)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(App: App): Builder

        fun build(): AppComponent
    }
}
