package com.created.team201.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.domain.model.Study
import com.created.domain.model.Todo
import com.created.domain.model.UserInfo
import com.created.team201.presentation.home.model.HomeUiModel
import com.created.team201.presentation.home.model.TodoUiModel

class HomeViewModel : ViewModel() {
    private val _userName: MutableLiveData<String> = MutableLiveData()
    val userName: LiveData<String> get() = _userName

    private val _userStudies: MutableLiveData<List<HomeUiModel>> = MutableLiveData(listOf())
    val userStudies: LiveData<List<HomeUiModel>> get() = _userStudies

    fun getUserStudyInfo() {
        _userName.value = DUMMY.userName
        _userStudies.value = DUMMY.studies.map { it.toUiModel() }
    }

    fun patchTodo(id: Int, isDone: Boolean) {
        // request: studyId, todoId, roundId, isDone
        // if(status == 200)

        val studies = userStudies.value ?: throw IllegalArgumentException()

        when (studies.any { it.necessaryTodo.todoId == id }) {
            true -> updateNecessaryTodoCheck(studies, id, isDone)
            false -> updateOptionalTodoCheck(studies, id, isDone)
        }
    }

    private fun updateNecessaryTodoCheck(studies: List<HomeUiModel>, id: Int, isDone: Boolean) {
        _userStudies.value = studies.map { homeUiModel ->
            homeUiModel.takeIf { it.necessaryTodo.todoId != id } ?: homeUiModel.copy(
                necessaryTodo = homeUiModel.necessaryTodo.copy(isDone = isDone),
            )
        }
    }

    private fun updateOptionalTodoCheck(studies: List<HomeUiModel>, id: Int, isDone: Boolean) {
        _userStudies.value = studies.map { homeUiModel ->
            homeUiModel.takeIf { todoUiModel -> !todoUiModel.optionalTodos.any { it.todoId == id } }
                ?: homeUiModel.copy(
                    optionalTodos = homeUiModel.optionalTodos.map {
                        it.takeUnless { it.todoId == id } ?: it.copy(isDone = isDone)
                    },
                )
        }
    }

    private fun Study.toUiModel(): HomeUiModel =
        HomeUiModel(
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
            listOf(
                Study(
                    1,
                    "빨리 만들자",
                    20,
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
                    87,
                    9,
                    "2023.07.02",
                    Todo(2, "선택 투두 끝내기", true),
                    listOf(Todo(3, "홈뷰 끝내기2", true)),

                ),
            ),
        )
    }
}
