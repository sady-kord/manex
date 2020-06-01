package com.baman.manex.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.baman.manex.data.dto.UserDto
import com.baman.manex.data.dto.WalletDto

@Dao
interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWallet(wallet : WalletDto)

    @Query("SELECT * FROM walletdto")
    fun findWallet(): LiveData<WalletDto>

}