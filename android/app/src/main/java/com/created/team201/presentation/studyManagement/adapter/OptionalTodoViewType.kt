package com.created.team201.presentation.studyManagement.adapter

import java.lang.IllegalArgumentException

enum class OptionalTodoViewType(val viewType: Int) {
    DISPLAY(0), ADD(1);

    companion object {
        fun valueOf(viewType: Int): OptionalTodoViewType {
            return values().find {
                it.viewType == viewType
            } ?: throw IllegalArgumentException("해당하는 뷰타입을 찾지 못했습니다.")
        }
    }
}
