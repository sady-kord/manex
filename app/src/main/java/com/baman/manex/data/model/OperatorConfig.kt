package com.baman.manex.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters

@Entity
@TypeConverters(OperatorTypeConverters::class)
class OperatorConfig(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val messages: Map<String, String>
)