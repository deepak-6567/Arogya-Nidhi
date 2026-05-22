package com.example.arogyanidhi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DocumentEntity::class, FormDataEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
    abstract fun formDataDao(): FormDataDao
}
