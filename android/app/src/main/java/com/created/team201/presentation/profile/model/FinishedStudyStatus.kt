package com.created.team201.presentation.profile.model

import com.created.team201.R

enum class FinishedStudyStatus(private val isSucceed: Boolean, private val resId: Int) {
    SUCCESS(true, R.drawable.ic_success),
    FAILURE(false, R.drawable.ic_failure),
    ;

    companion object {
        fun imageOf(isSucceed: Boolean): Int {
            return FinishedStudyStatus.values().find { image ->
                image.isSucceed == isSucceed
            }?.resId ?: throw IllegalAccessException("해당 스터디 상태를 찾을 수 없습니다.")
        }
    }
}
