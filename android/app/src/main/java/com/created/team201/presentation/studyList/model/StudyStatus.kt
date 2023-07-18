package com.created.team201.presentation.studyList.model

import com.created.team201.R

enum class StudyStatus(private val id: Int, private val resId: Int, private val strId: Int) {
    RECRUITING(0, R.drawable.ic_gathering, R.string.study_list_previous_date),
    PROCESSING(1, R.drawable.ic_processing, R.string.study_list_processing_date),
    END(2, R.drawable.ic_over, R.string.study_list_processing_date),
    ;

    companion object {
        fun imageOf(id: Int): Int {
            return StudyStatus.values().find { image ->
                image.id == id
            }?.resId ?: throw IllegalAccessException("해당 스터디 상태를 찾을 수 없습니다.")
        }

        fun dateFormatOf(id: Int): Int {
            return StudyStatus.values().find { dateFormat ->
                dateFormat.id == id
            }?.strId ?: throw IllegalAccessException("해당 스터디 상태를 찾을 수 없습니다.")
        }

        fun of(id: Int): StudyStatus {
            return StudyStatus.values().find { status ->
                status.id == id
            } ?: throw IllegalAccessException("해당 스터디를 찾을 수 없습니다.")
        }
    }
}
