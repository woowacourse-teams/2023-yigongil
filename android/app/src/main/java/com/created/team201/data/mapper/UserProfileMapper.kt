package com.created.team201.data.mapper

import com.created.domain.model.FinishedStudy
import com.created.domain.model.Period
import com.created.domain.model.PeriodUnit
import com.created.domain.model.Profile
import com.created.domain.model.UserProfile
import com.created.team201.data.remote.response.FinishedStudyResponseDto
import com.created.team201.data.remote.response.ProfileResponseDto

fun ProfileResponseDto.toDomain(): UserProfile = UserProfile(
    profile = Profile(
        githubId = githubId,
        id = id,
        introduction = introduction,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        successRate = successRate,
        successfulRoundCount = successfulRoundCount,
        tier = tier,
        tierProgress = tierProgress,
    ),
    finishedStudies = finishedStudies.map { it.toDomain() },
)

fun FinishedStudyResponseDto.toDomain(): FinishedStudy = FinishedStudy(
    id = id,
    averageTier = averageTier,
    isSucceed = isSucceed,
    name = name,
    numberOfCurrentMembers = numberOfCurrentMembers,
    numberOfMaximumMembers = numberOfMaximumMembers,
    periodOfRound = periodOfRound.toPeriod(),
    startAt = startAt,
    totalRoundCount = totalRoundCount,
)

private fun String.toPeriod(): Period =
    Period(Character.getNumericValue(first()), PeriodUnit.valueOf(last()))

