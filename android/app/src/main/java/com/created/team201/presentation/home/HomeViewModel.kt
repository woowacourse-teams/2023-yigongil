package com.created.team201.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Study
import com.created.domain.model.Todo
import com.created.domain.model.UserInfo
import com.created.domain.repository.HomeRepository
import com.created.team201.presentation.home.model.StudyUiModel
import com.created.team201.presentation.home.model.TodoUiModel

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _userName: MutableLiveData<String> = MutableLiveData()
    val userName: LiveData<String> get() = _userName

    private val _userStudies: MutableLiveData<List<StudyUiModel>> = MutableLiveData()
    val userStudies: LiveData<List<StudyUiModel>> get() = _userStudies

    fun getUserStudyInfo() {
        viewModelScope {
            runCatching {
                homeRepository.getUserStudies()
            }.onSuccess {
                _userName.value = it.userName
                _userStudies.value = it.studies.map { it.toUiModel() }

//                _userName.value = DUMMY.userName
//                _userStudies.value = DUMMY.studies.map { it.toUiModel() }
            }.onFailure { }
        }
    }

    fun patchTodo(id: Int, isDone: Boolean) {
        // network
        // request: studyId, todoId, roundId, isDone
        // if(status == 200)

        val studies = userStudies.value ?: throw IllegalArgumentException()

        when (studies.any { it.necessaryTodo.todoId == id }) {
            true -> updateNecessaryTodoCheck(studies, id, isDone)
            false -> updateOptionalTodoCheck(studies, id, isDone)
        }
    }

    private fun updateNecessaryTodoCheck(studies: List<StudyUiModel>, id: Int, isDone: Boolean) {
        _userStudies.value = studies.map { studyUiModel ->
            studyUiModel.takeIf { it.necessaryTodo.todoId != id } ?: studyUiModel.copy(
                necessaryTodo = studyUiModel.necessaryTodo.copy(isDone = isDone),
            )
        }
    }

    private fun updateOptionalTodoCheck(studies: List<StudyUiModel>, id: Int, isDone: Boolean) {
        _userStudies.value = studies.map { studyUiModel ->
            studyUiModel.takeIf { todoUiModel -> !todoUiModel.optionalTodos.any { it.todoId == id } }
                ?: studyUiModel.copy(
                    optionalTodos = studyUiModel.optionalTodos.map {
                        it.takeUnless { it.todoId == id } ?: it.copy(isDone = isDone)
                    },
                )
        }
    }

    private fun Study.toUiModel(): StudyUiModel =
        StudyUiModel(
            studyId = this.studyId.toLong(),
            studyName = this.studyName,
            progressRate = this.progressRate,
            leftDays = this.leftDays,
            nextDate = this.nextDate,
            necessaryTodo = this.necessaryTodo.toUiModel(),
            optionalTodos = this.optionalTodo.map { it.toUiModel() },
        )

    private fun Todo.toUiModel(): TodoUiModel = TodoUiModel(
        todoId = this.todoId,
        content = this.content,
        isDone = this.isDone,
    )

    companion object {
        private val DUMMY = UserInfo(
            "산군",
            2,
            listOf(
                Study(
                    1,
                    "빨리 만들자",
                    90,
                    5,
                    "2023.01.02",
                    Todo(2, "잔디구현끝내기", true),
                    listOf(
                        Todo(3, "홈뷰 끝내기", false),
                        Todo(4, "홈뷰 끝내기2", false),
                        Todo(5, "홈뷰 끝내기3", false),
                        Todo(6, "홈뷰 끝내기4", false),
                    ),

                ),
                Study(
                    2,
                    "빨리 만들자고",
                    57,
                    9,
                    "2023.07.02",
                    Todo(7, "선택 투두 끝내기", true),
                    listOf(Todo(8, "홈뷰 끝내기2", true)),

                ),
            ),
        )
    }
}
