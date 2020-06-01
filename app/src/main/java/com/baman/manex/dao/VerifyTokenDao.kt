package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.VerifyTokenDto

@Dao
interface VerifyTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToken(verifyToken: VerifyTokenDto)

    @Query("SELECT * FROM verifytokendto")
    fun findToken(): LiveData<VerifyTokenDto>

}
