package com.created.team201.presentation.studyList.model

import com.created.team201.R

enum class StudySummaryBackground(private val resId: Int) {
    FIRST(R.drawable.bg_rectangle_top_radius_5dp),
    MIDDLE(R.drawable.bg_3side_rectangle),
    LAST(R.drawable.bg_3side_rectangle_radius_bottom_5dp),
    ;

    companion object {
        fun of(position: Int, count: Int): Int {
            return when (position) {
                0 -> FIRST.resId
                count - 1 -> LAST.resId
                else -> MIDDLE.resId
            }
        }
    }
}
