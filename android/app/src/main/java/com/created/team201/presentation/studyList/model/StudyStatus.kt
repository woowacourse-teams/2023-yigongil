package com.created.team201.presentation.studyList.model

import com.created.team201.R

enum class StudyStatus(val id: Int, val resId: Int) {
    RECRUITING(0, R.drawable.ic_gathering),
    PROCESSING(1, R.drawable.ic_processing),
    END(2, R.drawable.ic_over),
    WAITING(3, R.drawable.ic_waiting),
    ;

    companion object {

        fun valueOf(id: Int): StudyStatus {
            return StudyStatus.values().find { status ->
                status.id == id
            } ?: throw IllegalAccessException("해당 스터디를 찾을 수 없습니다.")
        }
    }
}
