package com.created.team201.data.mapper

import com.created.domain.model.MustDo
import com.created.team201.data.remote.response.MustDoCertificationResponseDto

fun MustDoCertificationResponseDto.Me.toMustDo(): MustDo = MustDo(
    id = 0,
    isCertified = false,
    nickname = "",
    profileImageUrl = ""
)

fun MustDoCertificationResponseDto.Other.toMustDo(): MustDo = MustDo(
    id = 0,
    isCertified = false,
    nickname = "",
    profileImageUrl = ""
)

