package com.created.team201.data.repository

import com.created.domain.model.Certification
import com.created.domain.model.MemberCertification
import com.created.domain.repository.CertificationRepository
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.CertificationService
import com.created.team201.data.remote.api.ImageService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class DefaultCertificationRepository @Inject constructor(
    private val imageService: ImageService,
    private val certificationService: CertificationService,
) : CertificationRepository {

    override suspend fun postImage(file: File): Result<String> {
        val fileBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("image", file.name, fileBody)
        val response = imageService.postImage(image)
        return runCatching {
            response.headers()[LOCATION_KEY]
                ?: throw IllegalStateException()
        }
    }

    override suspend fun postCertification(
        studyId: Long,
        certification: Certification,
    ) {
        certificationService.postCertification(studyId, certification.toRequestDto())
    }

    override suspend fun getMemberCertification(
        studyId: Long,
        roundId: Long,
        memberId: Long,
    ): MemberCertification {
        return certificationService.getMemberCertification(studyId, roundId, memberId).toDomain()
    }

    companion object {
        private const val LOCATION_KEY = "Location"
    }
}
