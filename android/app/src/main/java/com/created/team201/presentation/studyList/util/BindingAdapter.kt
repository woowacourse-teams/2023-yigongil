package com.created.team201.presentation.studyList.util

import androidx.databinding.BindingAdapter
import com.created.team201.presentation.studyList.CustomChip
import com.created.team201.presentation.studyList.model.StudyListFilter

@BindingAdapter("app:studyListFilter")
fun setStudyListFilter(view: CustomChip, filter: StudyListFilter) {
    view.setFilterState(filter)
}
