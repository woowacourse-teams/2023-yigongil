package com.created.team201.data.mapper

import com.created.domain.model.Author
import com.created.domain.model.Certification
import com.created.domain.model.MemberCertification
import com.created.team201.data.remote.request.CertificationRequestDto
import com.created.team201.data.remote.response.AuthorResponseDto
import com.created.team201.data.remote.response.MemberCertificationResponseDto

fun Certification.toRequestDto(): CertificationRequestDto = CertificationRequestDto(content)

fun MemberCertificationResponseDto.toDomain(): MemberCertification =
    MemberCertification(id, author.toDomain(), imageUrl, content, createdAt)

fun AuthorResponseDto.toDomain(): Author = Author(id, nickname, profileImageUrl)
