package com.created.domain.repository

import com.created.domain.model.Certification
import com.created.domain.model.MemberCertification
import com.created.domain.model.response.NetworkResponse
import java.io.File

interface CertificationRepository {

    suspend fun postCertification(
        studyId: Long,
        imageUrl: File,
        certification: Certification,
    ): NetworkResponse<Unit>

    suspend fun getMemberCertification(
        studyId: Long,
        certificationsId: Long,
    ): MemberCertification
}
