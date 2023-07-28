package com.created.team201.presentation.studyManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.domain.model.Round
import com.created.team201.presentation.home.model.TodoUiModel
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel

class StudyManagementViewModel : ViewModel() {
    private val rounds: MutableLiveData<List<Round>> = MutableLiveData()
    private val _studyRounds: MutableLiveData<List<StudyRoundDetailUiModel>> = MutableLiveData()
    val studyRounds: LiveData<List<StudyRoundDetailUiModel>>
        get() = _studyRounds

    fun getStudyRounds(studyId: Long, currentRoundId: Long) {
        // to do : studyMaster id,와 비교해서 member에 isMaster 넣어줘야함
        _studyRounds.value = dummy
        getRounds(studyId)
        // getStudyRoundDetail() 여러번
        // _studyRounds.value = listOf()
    }

    private fun getRounds(studyId: Long) {
        rounds.value = listOf()
    }

    private fun getStudyRoundDetail(studyId: Long, roundId: Long) {
        // 서버 통신으로 round 정보 가져옴
    }

    fun updateTodo(currentItemId: Int, todoId: Long, isDone: Boolean) {
        val studyDetails = studyRounds.value ?: throw IllegalStateException()
        val currentStudy = studyRounds.value?.get(currentItemId) ?: throw IllegalStateException()
        val isNecessary = currentStudy.necessaryTodo.todoId == todoId
        val study: StudyRoundDetailUiModel
        val todo: TodoUiModel
        when (isNecessary) {
            true -> {
                updateNecessaryTodoCheck(studyDetails, todoId, isDone)
                study = studyDetails.find { it.necessaryTodo.todoId == todoId }!!
                todo = study.necessaryTodo
            }

            false -> {
                updateOptionalTodoCheck(studyDetails, todoId, isDone)
                study = studyDetails.find { it.optionalTodos.any { it.todo.todoId == todoId } }!!
                todo = study.optionalTodos.find { it.todo.todoId == todoId }!!.todo
            }
        }

        patchTodo(todo, study, isNecessary)
    }

    private fun patchTodo(todo: TodoUiModel, study: StudyRoundDetailUiModel, isNecessary: Boolean) {
        // 서버 통신으로 update
    }

    private fun updateNecessaryTodoCheck(
        studyDetails: List<StudyRoundDetailUiModel>,
        id: Long,
        isDone: Boolean,
    ) {
        _studyRounds.value = studyDetails.map { studyDetailUiModel ->
            studyDetailUiModel.takeIf { it.necessaryTodo.todoId != id } ?: studyDetailUiModel.copy(
                necessaryTodo = studyDetailUiModel.necessaryTodo.copy(isDone = isDone),
            )
        }
    }

    private fun updateOptionalTodoCheck(
        studyDetails: List<StudyRoundDetailUiModel>,
        id: Long,
        isDone: Boolean,
    ) {
        _studyRounds.value = studyDetails.map { studyDetailUiModel ->
            studyDetailUiModel.takeIf { todoUiModel -> !todoUiModel.optionalTodos.any { it.todo.todoId == id } }
                ?: studyDetailUiModel.copy(
                    optionalTodos = studyDetailUiModel.optionalTodos.map {
                        it.takeUnless { it.todo.todoId == id }
                            ?: it.copy(it.todo.copy(isDone = isDone))
                    },
                )
        }
    }

    fun addOptionalTodo(studyId: Long, currentPage: Int, todoContent: String) {
        val currentStudy = studyRounds.value?.get(currentPage)

        // 서버 통신 -> 투두 추가

        // 서버 통신 -> 다시 받아서 갈아끼워줘야함
    }
}
