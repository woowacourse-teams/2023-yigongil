package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyDetailResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("processingStatus")
    val processingStatus: Int,
    @SerialName("name")
    val name: String,
    @SerialName("numberOfCurrentMembers")
    val numberOfCurrentMembers: Int,
    @SerialName("numberOfMaximumMembers")
    val numberOfMaximumMembers: Int,
    @SerialName("studyMasterId")
    val studyMasterId: Long,
    @SerialName("meetingDaysCountPerWeek")
    val meetingDaysCountPerWeek: Int,
    @SerialName("introduction")
    val introduction: String,
    @SerialName("members")
    val members: List<MemberResponseDto>,
    @SerialName("minimumWeeks")
    val minimumWeeks: Int,
)
