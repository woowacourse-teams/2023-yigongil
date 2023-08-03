package com.created.team201.presentation.studyDetail.model

import androidx.annotation.StringRes
import com.created.team201.R

enum class PeriodFormat(@StringRes val res: Int) {
    WEEK(R.string.study_detail_period_of_count_week),
    DAY(R.string.study_detail_period_of_count_day),
    ;

    companion object {
        private const val DAY_SYMBOL = 'd'
        private const val WEEK_SYMBOL = 'w'

        fun valueOf(periodSymbol: Char): PeriodFormat {
            return when (periodSymbol) {
                DAY_SYMBOL -> DAY
                WEEK_SYMBOL -> WEEK
                else -> throw IllegalArgumentException("올바르지 않은 주기 표시입니다.")
            }
        }
    }
}
