package com.created.team201.data.mapper

import com.created.domain.model.StudyMember
import com.created.team201.data.remote.response.StudyMemberResponseDto


fun StudyMemberResponseDto.toDomain(): StudyMember = StudyMember(
    id = id,
    nickname = nickname ?: "",
    profileImageUrl = profileImageUrl ?: "",
    isDone = isDone,
    isDeleted = isDeleted,
)
