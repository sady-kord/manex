package com.baman.manex.di

import dagger.Module

@Module(includes = [ViewModelModule::class, NetworkModule::class, DatabaseModule::class])
class AppModule