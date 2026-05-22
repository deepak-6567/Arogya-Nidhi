package com.example.arogyanidhi.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("forms/submit")
    suspend fun submitFormData(
        @Body formData: Map<String, String>
    ): Response<Unit>
}
