package com.created.team201.data.repository

import com.created.domain.model.Certification
import com.created.domain.model.MemberCertification
import com.created.domain.model.response.NetworkResponse
import com.created.domain.repository.CertificationRepository
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.CertificationService
import com.created.team201.data.remote.request.CertificationRequestDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class DefaultCertificationRepository @Inject constructor(
    private val certificationService: CertificationService,
) : CertificationRepository {

    override suspend fun postCertification(
        studyId: Long,
        imageUrl: File,
        certification: Certification,
    ): NetworkResponse<Unit> {
        val fileBody = imageUrl.asRequestBody("image/*".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("imageUrl", imageUrl.name, fileBody)
        val body =
            Json.encodeToString(CertificationRequestDto.serializer(), certification.toRequestDto())
                .toRequestBody("application/json".toMediaType())
        return certificationService.postCertification(studyId, image, body)
    }

    override suspend fun getMemberCertification(
        studyId: Long,
        certificationsId: Long,
    ): MemberCertification {
        return certificationService.getMemberCertification(studyId, certificationsId).toDomain()
    }
}
