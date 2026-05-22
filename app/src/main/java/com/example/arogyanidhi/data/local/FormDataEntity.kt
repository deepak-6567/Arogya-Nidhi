package com.example.arogyanidhi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "form_data")
data class FormDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serviceName: String,
    val dataJson: String,
    val timestamp: Long = System.currentTimeMillis()
)
