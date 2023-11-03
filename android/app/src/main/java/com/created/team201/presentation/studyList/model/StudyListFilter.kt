package com.created.team201.presentation.studyList.model

enum class StudyListFilter {
    ALL, RECRUITING, PROCESSING, WAITING_APPLICANT, WAITING_MEMBER;

    companion object {
        fun StudyListFilter.isGuestOnly(): Boolean {
            return this == WAITING_MEMBER || this == WAITING_APPLICANT
        }
    }
}
