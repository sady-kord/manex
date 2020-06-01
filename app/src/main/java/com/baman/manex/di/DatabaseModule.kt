package com.baman.manex.di

import androidx.room.Room
import com.baman.manex.App
import com.baman.manex.dao.*
import com.baman.manex.db.ManexDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(app: App): ManexDb {
        return Room
            .databaseBuilder(app, ManexDb::class.java, "cache.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideOperatorConfigDao(db: ManexDb): OperatorConfigDao {
        return db.operatorConfigDao()
    }

    @Singleton
    @Provides
    fun provideStoreInfoDao(db: ManexDb): StoreInfoDao {
        return db.storeInfoDao()
    }

    @Singleton
    @Provides
    fun provideStoresDao(db: ManexDb): StoresDao {
        return db.storesDao()
    }

    @Singleton
    @Provides
    fun provideBurnDao(db: ManexDb): BurnDao {
        return db.burnDao()
    }


    @Singleton
    @Provides
    fun provideStoreByTypeDao(db: ManexDb): StoreByTypeDao {
        return db.storeByTypeDao()
    }

    @Singleton
    @Provides
    fun provideSortDao(db: ManexDb): SortDao {
        return db.sortDao()
    }

    @Singleton
    @Provides
    fun provideUserCardsDao(db: ManexDb): UserCardsDao {
        return db.userCardsDao()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: ManexDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideFilterDao(db: ManexDb): FilterDao {
        return db.filterDao()
    }

    @Singleton
    @Provides
    fun provideVerifyTokenDao(db: ManexDb): VerifyTokenDao {
        return db.verifyTokenDao()
    }

    @Singleton
    @Provides
    fun provideWalletDao(db: ManexDb): WalletDao{
        return db.WalletDao()
    }

    @Singleton
    @Provides
    fun provideValidationDao(db: ManexDb): ValidationDao{
        return db.ValidationDao()
    }

    @Singleton
    @Provides
    fun provideMessageDao(db: ManexDb): MessageDao{
        return db.messageDao()
    }
}
