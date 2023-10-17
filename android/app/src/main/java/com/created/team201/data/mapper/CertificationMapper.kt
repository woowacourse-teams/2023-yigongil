package com.created.team201.data.mapper

import com.created.domain.model.Certification
import com.created.team201.data.remote.request.CertificationRequestDto

fun Certification.toRequestDto(): CertificationRequestDto = CertificationRequestDto(content)
