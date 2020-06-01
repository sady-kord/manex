package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.SortDto

@Dao
interface SortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSort(sorts: List<SortDto>)

    @Query("SELECT * FROM sortdto")
    fun findSort(): LiveData<List<SortDto>>

}