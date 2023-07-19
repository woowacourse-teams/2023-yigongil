package com.created.team201.data.remote.request

import com.created.domain.model.Period
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStudyRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("numberOfMaximumMembers")
    val peopleCount: Int,
    @SerialName("startAt")
    val startDate: String,
    @SerialName("totalRoundCount")
    val period: Int,
    @SerialName("periodOfRound")
    val cycle: String,
    @SerialName("introduction")
    val introduction: String,
) {
    companion object {
        private const val TYPE_DAY = 0
        private const val TYPE_WEEK = 1
        fun getPeriodString(period: Period): String =
            when (period.type) {
                TYPE_DAY -> "${period.date}d"
                TYPE_WEEK -> "${period.type}w"
                else -> throw IllegalArgumentException("존재하지 않는 형식의 타입입니다.")
            }
    }
}
