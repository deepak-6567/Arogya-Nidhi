package com.example.arogyanidhi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FormDataDao {
    @Insert
    suspend fun insertFormData(formData: FormDataEntity)

    @Query("SELECT * FROM form_data ORDER BY timestamp DESC")
    fun getAllFormData(): Flow<List<FormDataEntity>>
}
