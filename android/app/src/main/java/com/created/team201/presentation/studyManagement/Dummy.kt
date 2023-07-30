package com.created.team201.presentation.studyManagement

import com.created.domain.model.Role
import com.created.domain.model.Round
import com.created.team201.presentation.home.model.TodoUiModel
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel
import com.created.team201.presentation.studyManagement.model.StudyMemberUiModel
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel

val dummy: List<StudyRoundDetailUiModel> = listOf(
    StudyRoundDetailUiModel(
        1L,
        11L,
        Role.MASTER,
        TodoUiModel(1L, "책읽기 모두", true),
        listOf(
            OptionalTodoUiModel(TodoUiModel(2L, "블로그 정리", false), 0),
            OptionalTodoUiModel(TodoUiModel(3L, "블로그 정리22", false), 0),
        ),
        listOf(
            StudyMemberUiModel(1L, true, "반달", "", true),
            StudyMemberUiModel(2L, false, "산군", "", false),
            StudyMemberUiModel(3L, false, "써니", "", true),
            StudyMemberUiModel(4L, false, "링링", "", true),
        ),
    ),
    StudyRoundDetailUiModel(
        2L,
        11L,
        Role.MASTER,
        TodoUiModel(1L, "책읽기 모두", true),
        listOf(
            OptionalTodoUiModel(TodoUiModel(2L, "블로그 정리", false), 0),
            OptionalTodoUiModel(TodoUiModel(3L, "블로그 정리22", false), 0),
        ),
        listOf(
            StudyMemberUiModel(1L, true, "반달", "", true),
            StudyMemberUiModel(2L, false, "산군", "", true),
            StudyMemberUiModel(3L, false, "써니", "", true),
            StudyMemberUiModel(4L, false, "링링", "", true),
        ),
    ),
    StudyRoundDetailUiModel(
        3L,
        11L,
        Role.MASTER,
        TodoUiModel(1L, "책읽기 모두", true),
        listOf(
            OptionalTodoUiModel(TodoUiModel(2L, "블로그 정리", false), 0),
            OptionalTodoUiModel(TodoUiModel(3L, "블로그 정리22", false), 0),
        ),
        listOf(
            StudyMemberUiModel(1L, true, "반달", "", true),
            StudyMemberUiModel(2L, false, "산군", "", true),
            StudyMemberUiModel(3L, false, "써니", "", true),
            StudyMemberUiModel(4L, false, "링링", "", true),
        ),
    ),
)

val roundDummy = listOf(
    Round(1L, 1),
    Round(2L, 2),
    Round(3L, 3),
)
