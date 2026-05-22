package com.example.arogyanidhi.data.repository

import android.util.Log
import com.example.arogyanidhi.data.local.FormDataDao
import com.example.arogyanidhi.data.local.FormDataEntity
import com.example.arogyanidhi.data.remote.ApiService
import com.example.arogyanidhi.domain.repository.FormDataRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FormDataRepositoryImpl @Inject constructor(
    private val formDataDao: FormDataDao,
    private val apiService: ApiService
) : FormDataRepository {

    override suspend fun saveFormData(serviceName: String, data: Map<String, String>) {
        // 1. Save locally
        val json = Json.encodeToString(data)
        val entity = FormDataEntity(
            serviceName = serviceName,
            dataJson = json
        )
        formDataDao.insertFormData(entity)
        
        // 2. Sync with Backend
        try {
            val response = apiService.submitFormData(data + ("service_name" to serviceName))
            if (response.isSuccessful) {
                Log.d("FormDataRepo", "Successfully synced with backend")
            } else {
                Log.e("FormDataRepo", "Backend sync failed: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("FormDataRepo", "Error syncing with backend", e)
        }
    }

    override fun getAllFormData(): Flow<List<FormDataEntity>> {
        return formDataDao.getAllFormData()
    }
}
