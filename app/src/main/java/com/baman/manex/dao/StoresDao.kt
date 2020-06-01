package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.StoresDTO

@Dao
interface StoresDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStore(storesDTO: StoresDTO)

    @Query("SELECT * FROM storesdto")
    fun findStores(): LiveData<StoresDTO>

}