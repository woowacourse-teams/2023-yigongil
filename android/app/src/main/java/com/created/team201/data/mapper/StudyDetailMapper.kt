package com.created.team201.data.mapper

import com.created.domain.model.Member
import com.created.domain.model.Round
import com.created.domain.model.StudyDetail
import com.created.team201.data.remote.response.MemberResponseDto
import com.created.team201.data.remote.response.RoundResponseDto
import com.created.team201.data.remote.response.StudyDetailResponseDto

fun StudyDetailResponseDto.toDomain(): StudyDetail = StudyDetail(
    id = id,
    processingStatus = processingStatus,
    name = name,
    numberOfCurrentMembers = numberOfCurrentMembers,
    numberOfMaximumMembers = numberOfMaximumMembers,
    studyMasterId = studyMasterId,
    meetingDaysCountPerWeek = meetingDaysCountPerWeek,
    introduction = introduction,
    members = members.map { it.toDomain() },
    minimumWeeks = minimumWeeks,
)

fun MemberResponseDto.toDomain(): Member = Member(
    id = this.id,
    tier = this.tier,
    nickname = this.nickname ?: "",
    successRate = this.successRate,
    profileImage = this.profileImage ?: "",
    isDeleted = this.isDeleted,
)

fun RoundResponseDto.toDomain(): Round = Round(
    id = this.id,
    number = this.number,
)
