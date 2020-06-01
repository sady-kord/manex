package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.baman.manex.data.dto.UserDto

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(cards: UserDto)

    @Query("SELECT * FROM userdto")
    fun findUser(): LiveData<UserDto>


    @Query("SELECT * FROM userdto")
    fun getUser(): UserDto

    @Update
    fun updateUser(userDto: UserDto)

}