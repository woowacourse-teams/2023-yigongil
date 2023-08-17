package com.created.team201.data.mapper

import com.created.domain.model.ProfileInformation
import com.created.team201.data.remote.request.MyProfileRequestDTO

fun ProfileInformation.toRequestDto(): MyProfileRequestDTO = MyProfileRequestDTO(
    nickname = nickname.nickname,
    introduction = introduction,
)
