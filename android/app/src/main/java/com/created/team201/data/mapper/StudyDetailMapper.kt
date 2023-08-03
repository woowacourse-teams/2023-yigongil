package com.created.team201.data.mapper

import com.created.domain.model.Member
import com.created.domain.model.Round
import com.created.domain.model.StudyDetail
import com.created.team201.data.remote.response.MemberResponseDto
import com.created.team201.data.remote.response.RoundResponseDto
import com.created.team201.data.remote.response.StudyDetailResponseDto

fun StudyDetailResponseDto.toDomain(): StudyDetail = StudyDetail(
    id = this.id,
    processingStatus = this.processingStatus,
    name = this.name,
    numberOfCurrentMembers = this.numberOfCurrentMembers,
    numberOfMaximumMembers = this.numberOfMaximumMembers,
    studyMasterId = this.studyMasterId,
    role = this.role,
    startAt = this.startAt,
    totalRoundCount = this.totalRoundCount,
    periodOfRound = this.periodOfRound,
    currentRound = this.currentRound,
    introduction = this.introduction,
    members = this.members.map { it.toDomain() },
    rounds = this.rounds.map { it.toDomain() },
)

fun MemberResponseDto.toDomain(): Member = Member(
    id = this.id,
    tier = this.tier,
    nickname = this.nickname,
    successRate = this.successRate.toInt(),
    profileImage = this.profileImage ?: "",
)

fun RoundResponseDto.toDomain(): Round = Round(
    id = this.id,
    number = this.number,
)
