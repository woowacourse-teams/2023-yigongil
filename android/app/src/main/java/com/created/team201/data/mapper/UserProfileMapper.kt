package com.created.team201.data.mapper

import com.created.domain.model.FinishedStudy
import com.created.domain.model.Nickname
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
import com.created.domain.model.UserProfile
import com.created.team201.data.model.FinishedStudyEntity
import com.created.team201.data.model.UserProfileEntity
import com.created.team201.data.remote.response.FinishedStudyResponseDto
import com.created.team201.data.remote.response.ProfileResponseDto

fun ProfileResponseDto.toDomain(): UserProfile = UserProfile(
    profile = Profile(
        githubId = githubId,
        id = id,
        profileInformation = ProfileInformation(Nickname(nickname), introduction),
        profileImageUrl = profileImageUrl,
        successRate = successRate,
        successfulRoundCount = successfulRoundCount,
        tier = tier,
        tierProgress = tierProgress,
    ),
    finishedStudies = finishedStudies.map { it.toDomain() },
)

fun ProfileResponseDto.toEntity(): UserProfileEntity = UserProfileEntity(
    profile = Profile(
        githubId = githubId,
        id = id,
        profileInformation = ProfileInformation(Nickname(nickname), introduction),
        profileImageUrl = profileImageUrl,
        successRate = successRate,
        successfulRoundCount = successfulRoundCount,
        tier = tier,
        tierProgress = tierProgress,
    ),
    finishedStudies = finishedStudies.map { it.toEntity() },
)

fun FinishedStudyResponseDto.toDomain(): FinishedStudy = FinishedStudy(
    id = id,
    name = name,
    averageTier = averageTier,
    numberOfCurrentMembers = numberOfCurrentMembers,
    numberOfMaximumMembers = numberOfMaximumMembers,
    isSucceed = isSucceed,
)

fun FinishedStudyResponseDto.toEntity(): FinishedStudyEntity = FinishedStudyEntity(
    id = id,
    name = name,
    averageTier = averageTier,
    numberOfCurrentMembers = numberOfCurrentMembers,
    numberOfMaximumMembers = numberOfMaximumMembers,
    isSucceed = isSucceed,
)
