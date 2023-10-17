package com.created.team201.presentation.studyList

import android.content.Context
import android.util.AttributeSet
import com.created.team201.presentation.studyList.model.StudyListFilter
import com.google.android.material.chip.Chip

class CustomChip(context: Context, attrs: AttributeSet) : Chip(context, attrs) {
    private lateinit var _filter: StudyListFilter
    val filter: StudyListFilter
        get() = _filter

    fun setFilterState(filter: StudyListFilter) {
        _filter = filter
    }
}
