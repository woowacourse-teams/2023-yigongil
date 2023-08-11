package com.created.team201.presentation.studyDetail.model

enum class StudyPeopleViewType(val viewType: Int) {
    MEMBER(0), DELETED_MEMBER(1);

    companion object {
        fun valueOf(viewType: Int): StudyPeopleViewType {
            return StudyPeopleViewType.values().find {
                it.viewType == viewType
            } ?: throw IllegalArgumentException("해당하는 뷰타입을 찾지 못했습니다.")
        }
    }
}
