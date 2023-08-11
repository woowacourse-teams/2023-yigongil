package com.created.team201.presentation.studyManage.model

import androidx.annotation.StringRes
import com.created.team201.R

enum class MyStudyStatus(val id: Long, @StringRes val notice: Int) {
    PARTICIPATED(0, R.string.study_manage_joined_tab_no_study),
    OPENED(1, R.string.study_manage_opened_tab_no_study),
}
