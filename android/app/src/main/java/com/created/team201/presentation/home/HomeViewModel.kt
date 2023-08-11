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
        viewModelScope.launch {
            runCatching {
                homeRepository.getUserStudies()
            }.onSuccess { result ->
                _userName.value = result.userName
                _userStudies.value = result.studies.map { it.toUiModel() }
            }.onFailure {
                Log.d("ERROR", it.toString())
            }
        }
    }

    fun updateTodo(todoId: Long, isDone: Boolean) {
        // 투두 항목이 비어 있을 경우 체크 불가
        // 투두 항목이 비어 있을 경우, 체크 버튼 숨김
        // 디데이순 스터디 카드 출력
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
                Log.d("투두 통신 성공", "투두 통신 성공")
            }.onFailure {
                Log.d("투두 에러", it.message.toString())
            }
        }
    }

    private fun updateNecessaryTodoCheck(studies: List<StudyUiModel>, id: Long, isDone: Boolean) {
        _userStudies.value = studies.map { studyUiModel ->
            studyUiModel.takeIf { it.necessaryTodo.todoId != id } ?: studyUiModel.copy(
                necessaryTodo = studyUiModel.necessaryTodo.copy(isDone = isDone),
            )
        }
    }

    private fun updateOptionalTodoCheck(studies: List<StudyUiModel>, id: Long, isDone: Boolean) {
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
        content = this.content ?: "",
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
    }
}
