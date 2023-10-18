package com.created.team201.data.remote.api

import com.created.domain.model.response.NetworkResponse
import com.created.team201.data.remote.response.MemberCertificationResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
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

    @GET("v1/studies/{studyId}/rounds/{roundId}/members/{memberId}")
    suspend fun getMemberCertification(
        @Path("studyId") studyId: Long,
        @Path("roundId") roundId: Long,
        @Path("memberId") memberId: Long,
    ): MemberCertificationResponseDto
}
