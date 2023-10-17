package com.created.domain.repository

import com.created.domain.model.Certification
import com.created.domain.model.response.NetworkResponse
import java.io.File

interface CertificationRepository {

    suspend fun postCertification(
        studyId: Long,
        imageUrl: File,
        certification: Certification,
    ): NetworkResponse<Unit>
}
