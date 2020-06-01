package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.StoreByTypeDto


@Dao
interface StoreByTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStore(storeByTypeDto: StoreByTypeDto)

    @Query("SELECT * FROM storebytypedto")
    fun findStores(): LiveData<StoreByTypeDto>

}