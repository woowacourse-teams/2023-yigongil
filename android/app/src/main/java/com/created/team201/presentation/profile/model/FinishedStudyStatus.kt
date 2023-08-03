package com.created.team201.presentation.profile.model

import androidx.annotation.DrawableRes
import com.created.team201.R

enum class FinishedStudyStatus(
    private val isSucceed: Boolean,
    @DrawableRes private val resId: Int,
) {
    SUCCESS(true, R.drawable.ic_success),
    FAILURE(false, R.drawable.ic_failure),
    ;

    companion object {
        fun imageOf(isSucceed: Boolean): Int {
            return FinishedStudyStatus.values().find { status ->
                status.isSucceed == isSucceed
            }?.resId ?: throw IllegalArgumentException("해당 스터디 상태를 찾을 수 없습니다.")
        }
    }
}
