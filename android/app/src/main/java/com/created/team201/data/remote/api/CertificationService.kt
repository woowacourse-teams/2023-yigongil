package com.created.team201.data.remote.api

import com.created.domain.model.response.NetworkResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CertificationService {

    @Multipart
    @POST("studies/{studyId}/certification")
    suspend fun postCertification(
        @Path("studyId") studyId: Long,
        @Part imageUrl: MultipartBody.Part,
        @Part("request") body: RequestBody,
    ): NetworkResponse<Unit>
}
