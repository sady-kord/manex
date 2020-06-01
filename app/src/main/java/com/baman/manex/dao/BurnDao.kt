package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.BurnDto

@Dao
interface BurnDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBurn(burnDTO: BurnDto)

    @Query("SELECT * FROM burndto")
    fun findBurn(): LiveData<BurnDto>

}