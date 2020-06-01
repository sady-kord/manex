package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baman.manex.data.dto.BurnDto
import com.baman.manex.data.dto.MessageDto

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(messages: List<MessageDto>)

    @Query("SELECT * FROM messagedto")
    fun findMessages(): LiveData<List<MessageDto>>

    @Query("SELECT * FROM messagedto WHERE `key` = :errorKey")
    fun getMessages(errorKey : String): LiveData<MessageDto>

}