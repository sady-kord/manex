package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.UserCardsDto

@Dao
interface UserCardsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserCards(cards: List<UserCardsDto>)

    @Query("SELECT * FROM usercardsdto")
    fun findAll(): LiveData<List<UserCardsDto>>
}