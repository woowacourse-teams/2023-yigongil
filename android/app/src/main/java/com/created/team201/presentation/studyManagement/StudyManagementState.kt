package com.created.team201.presentation.studyManagement

import androidx.annotation.StringRes
import com.created.domain.model.Role
import com.created.team201.R

sealed class StudyManagementState(
    @StringRes
    val title: Int,
    @StringRes
    val buttonText: Int,
) {
    object Master : StudyManagementState(
        R.string.study_management_app_bar_title_is_study_master,
        R.string.study_management_end_button_is_study_master,
    )

    object Member : StudyManagementState(
        R.string.study_management_app_bar_title_is_study_participant,
        R.string.study_management_end_button_is_study_participant,
    )

    companion object {
        fun getRoleStatus(role: Role): StudyManagementState {
            if (role == Role.MASTER) return Master
            return Member
        }
    }
}
