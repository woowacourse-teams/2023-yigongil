package com.created.domain.repository

import com.created.domain.model.Certification
import com.created.domain.model.MemberCertification
import java.io.File

interface CertificationRepository {

    suspend fun postImage(
        file: File,
    ): Result<String>

    suspend fun postCertification(
        studyId: Long,
        certification: Certification,
    )

    suspend fun getMemberCertification(
        studyId: Long,
        roundId: Long,
        memberId: Long,
    ): MemberCertification
}
