package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.StoresDTO
import com.baman.manex.data.dto.ValidationDto

@Dao
interface ValidationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertValidation(validationDto: ValidationDto)

    @Query("SELECT * FROM validationdto")
    fun findValidation(): LiveData<ValidationDto>

}