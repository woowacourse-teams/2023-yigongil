package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.CertificationRequestDto
import com.created.team201.data.remote.response.MemberCertificationResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CertificationService {

    @POST("v1/studies/{id}/certifications")
    suspend fun postCertification(
        @Path("id") studyId: Long,
        @Body certification: CertificationRequestDto,
    )

    @GET("v1/studies/{studyId}/rounds/{roundId}/members/{memberId}")
    suspend fun getMemberCertification(
        @Path("studyId") studyId: Long,
        @Path("roundId") roundId: Long,
        @Path("memberId") memberId: Long,
    ): MemberCertificationResponseDto
}
