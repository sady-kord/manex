package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.FilterDto

@Dao
interface FilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilter(stores: FilterDto)

    @Query("SELECT * FROM filterdto")
    fun findFilter(): LiveData<FilterDto>
}

