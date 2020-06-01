package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.StoreInfoDto

@Dao
interface StoreInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStore(stores: List<StoreInfoDto>)

    @Query("SELECT * FROM storeinfodto")
    fun findStores(): LiveData<List<StoreInfoDto>>
}