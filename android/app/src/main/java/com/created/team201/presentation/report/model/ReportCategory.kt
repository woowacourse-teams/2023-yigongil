package com.created.team201.presentation.report.model

enum class ReportCategory(val index: Int) {
    STUDY(0),
    USER(1),
    ;

    companion object {
        fun valueOf(index: Int): ReportCategory {
            return ReportCategory.values().find {
                it.index == index
            } ?: throw IllegalArgumentException("해당하는 신고 카테고리를 찾지 못했습니다.")
        }
    }
}
