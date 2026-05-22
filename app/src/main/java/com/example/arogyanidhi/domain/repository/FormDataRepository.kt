package com.example.arogyanidhi.domain.repository

import com.example.arogyanidhi.data.local.FormDataEntity
import kotlinx.coroutines.flow.Flow

interface FormDataRepository {
    suspend fun saveFormData(serviceName: String, data: Map<String, String>)
    fun getAllFormData(): Flow<List<FormDataEntity>>
}
