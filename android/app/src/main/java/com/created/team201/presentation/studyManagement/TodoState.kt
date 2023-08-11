package com.created.team201.presentation.studyManagement

sealed interface TodoState {
    object DEFAULT : TodoState

    object NECESSARY_TODO_EDIT : TodoState

    object OPTIONAL_TODO_EDIT : TodoState

    object OPTIONAL_TODO_ADD : TodoState

    object NOTHING : TodoState
}
