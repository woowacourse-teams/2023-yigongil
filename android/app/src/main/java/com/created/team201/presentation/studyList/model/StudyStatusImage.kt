package com.created.team201.presentation.studyList.model

import com.created.team201.R

enum class StudyStatusImage(private val id: Int, private val resId: Int) {
    RECRUITING(1, R.drawable.ic_gathering),
    PROCESSING(2, R.drawable.ic_processing),
    END(3, R.drawable.ic_over),
    ;

    companion object {
        fun valueOf(id: Int): Int {
            return StudyStatusImage.values().find { image ->
                image.id == id
            }?.resId ?: throw IllegalAccessException("해당 스터디 상태를 찾을 수 없습니다.")
        }
    }
}