package com.baman.manex.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baman.manex.dao.*
import com.baman.manex.data.dto.*
import com.baman.manex.data.model.OperatorConfig

/**
 * Main database description.
 */
@Database(
    entities =
    [
        OperatorConfig::class,
        SortDto::class,
        StoreByTypeDto::class,
        StoresDTO::class,
        UserCardsDto::class,
        StoreInfoDto::class,
        UserDto::class,
        BurnDto::class,
        FilterDto::class,
        VoucherDetailDto::class,
        VerifyTokenDto::class,
        WalletDto::class,
        ValidationDto::class,
        MessageDto::class
    ],
    version = 59, exportSchema = false
)
abstract class ManexDb : RoomDatabase() {

    abstract fun operatorConfigDao(): OperatorConfigDao

    abstract fun storeInfoDao(): StoreInfoDao

    abstract fun storesDao(): StoresDao

    abstract fun burnDao(): BurnDao

    abstract fun storeByTypeDao(): StoreByTypeDao

    abstract fun sortDao(): SortDao

    abstract fun userCardsDao(): UserCardsDao

    abstract fun userDao(): UserDao

    abstract fun filterDao(): FilterDao

    abstract fun verifyTokenDao(): VerifyTokenDao

    abstract fun WalletDao(): WalletDao

    abstract fun ValidationDao(): ValidationDao

    abstract fun messageDao(): MessageDao

}
