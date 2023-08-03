package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.MemberResponseDto
import com.created.team201.data.remote.response.StudyDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface StudyDetailService {
    @GET("/v1/studies/{studyId}")
    suspend fun getStudyDetail(
        @Path("studyId") studyId: Long,
    ): StudyDetailResponseDto

    @GET("/v1/studies/{studyId}/applicants")
    suspend fun getStudyApplicants(
        @Path("studyId") studyId: Long,
    ): ArrayList<MemberResponseDto>

    @POST("/v1/studies/{studyId}/applicants")
    suspend fun participateStudy(
        @Path("studyId") studyId: Long,
    )

    @PATCH("/v1/studies/{studyId}/start")
    suspend fun startStudy(
        @Path("studyId") studyId: Long,
    )

    @PATCH("/v1/studies/{studyId}/applicants/{memberId}")
    suspend fun acceptApplicant(
        @Path("studyId") studyId: Long,
        @Path("memberId") memberId: Long,
    )
}
