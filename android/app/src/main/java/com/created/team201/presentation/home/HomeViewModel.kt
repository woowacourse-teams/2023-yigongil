package com.created.team201.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.model.Study
import com.created.domain.model.Todo
import com.created.domain.model.UserInfo
import com.created.domain.repository.HomeRepository
import com.created.team201.data.datasource.remote.HomeDataSourceImpl
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.HomeRepositoryImpl
import com.created.team201.presentation.home.model.StudyUiModel
import com.created.team201.presentation.home.model.TodoUiModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _userName: MutableLiveData<String> = MutableLiveData()
    val userName: LiveData<String> get() = _userName

    private val _userStudies: MutableLiveData<List<StudyUiModel>> = MutableLiveData()
    val userStudies: LiveData<List<StudyUiModel>> get() = _userStudies

    init {
        updateUserStudies()
    }

    fun updateUserStudies() {
        _userStudies.value = DUMMY.studies.map { it.toUiModel() }

        viewModelScope.launch {
            runCatching {
                homeRepository.getUserStudies()
            }.onSuccess { result ->
                _userName.value = result.userName
//                _userStudies.value = result.studies.map { it.toUiModel() }
            }.onFailure {
                Log.d("123123", "123123")
            }
        }
    }

    fun updateTodo(todoId: Int, isDone: Boolean) {
        val studies = userStudies.value ?: throw IllegalArgumentException()
        val isNecessary = studies.any { it.necessaryTodo.todoId == todoId }
        val study: StudyUiModel
        val todo: TodoUiModel

        when (isNecessary) {
            true -> {
                updateNecessaryTodoCheck(studies, todoId, isDone)
                study = studies.find { it.necessaryTodo.todoId == todoId }!!
                todo = study.necessaryTodo
            }

            false -> {
                updateOptionalTodoCheck(studies, todoId, isDone)
                study = studies.find { it.optionalTodos.any { it.todoId == todoId } }!!
                todo = study.optionalTodos.find { it.todoId == todoId }!!
            }
        }

        patchTodo(todo, study, isNecessary)
    }

    private fun patchTodo(todo: TodoUiModel, study: StudyUiModel, isNecessary: Boolean) {
        viewModelScope.launch {
            runCatching {
                homeRepository.patchTodo(todo.toDomain(), study.studyId, isNecessary)
            }.onSuccess { result ->
                Log.d("123123", "1231555555523")
            }.onFailure {
                Log.d("123123", it.message.toString())
            }
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
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    homeRepository = HomeRepositoryImpl(
                        HomeDataSourceImpl(NetworkServiceModule.homeService),
                    ),
                )
            }
        }

        private val DUMMY = UserInfo(
            "산군",
            2,
            listOf(
                Study(
                    2,
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
