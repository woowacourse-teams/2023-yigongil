package com.created.team201.presentation.studyManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.domain.model.PageIndex
import com.created.domain.model.Role
import com.created.domain.model.Round
import com.created.team201.presentation.home.model.TodoUiModel
import com.created.team201.presentation.studyManagement.model.StudyManagementInformationUiModel
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel

class StudyManagementViewModel : ViewModel() {
    private val rounds: MutableLiveData<List<Round>> = MutableLiveData()
    private val _studyRounds: MutableLiveData<List<StudyRoundDetailUiModel>> = MutableLiveData()
    val studyRounds: LiveData<List<StudyRoundDetailUiModel>>
        get() = _studyRounds
    private val _state: MutableLiveData<StudyManagementState> =
        MutableLiveData(StudyManagementState.Member)
    val state: LiveData<StudyManagementState> get() = _state
    private val _currentRound: MutableLiveData<Int> = MutableLiveData(0)
    val currentRound: LiveData<Int> get() = _currentRound
    lateinit var studyInformation: StudyManagementInformationUiModel

    fun fetchStudyInformation() {
        // dummy
        studyInformation = StudyManagementInformationUiModel(
            "자바 스터디", 1, 3, Role.MASTER, "2022.03.04", 3, "3d", 2, "설명입니다.",
        )
        // 여기서 서버 호출해서 다 가지고 있게 하자 ~
    }

    fun getStudyRounds(studyId: Long, currentRoundId: Long) {
        // current Round 초기화
        _currentRound.value = studyInformation.currentRound
        // to do : studyMaster id,와 비교해서 member에 isMaster 넣어줘야함
        _studyRounds.value = dummy
        _state.value = StudyManagementState.Member // Role을 통해 현재 무슨 역할인지 분기처리
        getRounds(studyId)
        // getStudyRoundDetail() 여러번
        // _studyRounds.value = listOf()
    }

    private fun getRounds(studyId: Long) {
        rounds.value = roundDummy
    }

    private fun getStudyRoundDetail(studyId: Long, roundId: Long) {
        // 서버 통신으로 round 정보 가져옴
    }

    fun updateCurrentPage(pageIndex: PageIndex) {
        val rounds = rounds.value ?: listOf()
        val studyRounds = studyRounds.value ?: listOf()

        val round = rounds.find {
            it.id == studyRounds[pageIndex.number].id
        } ?: throw IllegalStateException("해당 스터디 없음")

        _currentRound.value = round.number
    }

    fun fetchRoundDetail(pageIndex: PageIndex) {
        // currentRoundId 갱신
        updateCurrentPage(pageIndex)
        // getStudyRoundDetail
        // 서버 통신
        // 인덱스 0일때는 앞에거...
        // 인덱스 마지막일때는.. 뒤에거..
    }

    fun updateTodo(currentItemId: Int, todoId: Long, isDone: Boolean) {
        val studyDetails = studyRounds.value ?: listOf()
        val isNecessary = studyRounds.value?.get(currentItemId)?.necessaryTodo?.todoId == todoId
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
