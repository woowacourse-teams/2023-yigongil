package com.created.team201.presentation.studyList

import android.content.Context
import android.util.AttributeSet
import com.created.team201.presentation.studyList.model.StudyListFilter
import com.google.android.material.chip.Chip

class CustomChip(context: Context, attrs: AttributeSet) : Chip(context, attrs) {
    lateinit var filter: StudyListFilter

    fun setFilterState(filter: StudyListFilter) {
        this.filter = filter
    }
}
