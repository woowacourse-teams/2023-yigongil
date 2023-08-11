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
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.HomeRepositoryImpl
import com.created.team201.presentation.home.model.StudyUiModel
import com.created.team201.presentation.home.model.TodoWithRoundIdUiModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _userName: MutableLiveData<String> = MutableLiveData()
    val userName: LiveData<String> get() = _userName

    private val _userStudies: MutableLiveData<List<StudyUiModel>> = MutableLiveData()
    val userStudies: LiveData<List<StudyUiModel>> get() = _userStudies

    private val _isStudyExistence: MutableLiveData<Boolean> = MutableLiveData(false)
    val isStudyExistence: LiveData<Boolean> get() = _isStudyExistence

    fun updateUserStudies() {
        viewModelScope.launch {
            runCatching {
                homeRepository.getUserStudies()
            }.onSuccess { result ->
                _userName.value = result.userName
                _userStudies.value = result.studies.map { it.toUiModel() }.sortedBy { it.leftDays }
                if (!userStudies.value.isNullOrEmpty()) _isStudyExistence.value = true
            }.onFailure {
                Log.d("ERROR", it.toString())
            }
        }
    }

    fun updateNecessaryTodo(todo: TodoWithRoundIdUiModel, isDone: Boolean) {
        updateNecessaryTodoCheck(todo.todoId, isDone)

        viewModelScope.launch {
            homeRepository.patchNecessaryTodo(todo.roundId, isDone)
                .onSuccess { }
                .onFailure { }
        }
    }

    fun updateOptionalTodo(todo: TodoWithRoundIdUiModel, isDone: Boolean) {
        updateOptionalTodoCheck(todo.todoId, isDone)

        viewModelScope.launch {
            homeRepository.patchOptionalTodo(todo.toDomain(isDone), todo.roundId)
                .onSuccess { }
                .onFailure { }
        }
    }

    private fun updateNecessaryTodoCheck(id: Long, isDone: Boolean) {
        _userStudies.value = userStudies.value?.map { studyUiModel ->
            studyUiModel.takeIf { it.necessaryTodo.todoId != id } ?: studyUiModel.copy(
                necessaryTodo = studyUiModel.necessaryTodo.copy(isDone = isDone),
            )
        }
    }

    private fun updateOptionalTodoCheck(id: Long, isDone: Boolean) {
        _userStudies.value = userStudies.value?.map { studyUiModel ->
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
            studyId = studyId.toLong(),
            studyName = studyName,
            progressRate = progressRate,
            leftDays = leftDays,
            nextDate = nextDate,
            necessaryTodo = necessaryTodo.toUiModel(roundId),
            optionalTodos = optionalTodo.map { it.toUiModel(roundId) },
        )

    private fun Todo.toUiModel(roundId: Int): TodoWithRoundIdUiModel = TodoWithRoundIdUiModel(
        todoId = todoId,
        content = content ?: "",
        isDone = isDone,
        roundId = roundId,
    )

    private fun TodoWithRoundIdUiModel.toDomain(isDone: Boolean): Todo = Todo(
        todoId = todoId,
        content = content,
        isDone = isDone,

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
