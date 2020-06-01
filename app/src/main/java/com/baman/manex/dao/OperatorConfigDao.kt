package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.model.OperatorConfig

@Dao
interface OperatorConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(config: OperatorConfig)

    @Query("SELECT * FROM operatorConfig")
    fun findAll(): LiveData<OperatorConfig>

}
