package com.created.team201.presentation.studyManagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.created.domain.model.CreateTodo
import com.created.domain.model.PageIndex
import com.created.domain.model.Round
import com.created.domain.model.RoundDetail
import com.created.domain.model.StudyDetail
import com.created.domain.model.StudyMember
import com.created.domain.model.Todo
import com.created.domain.repository.StudyManagementRepository
import com.created.team201.data.datasource.remote.StudyManagementDataSourceImpl
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.StudyManagementRepositoryImpl
import com.created.team201.presentation.home.model.TodoUiModel
import com.created.team201.presentation.studyManagement.adapter.OptionalTodoViewType
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel
import com.created.team201.presentation.studyManagement.model.RoundUiModel
import com.created.team201.presentation.studyManagement.model.StudyManagementInformationUiModel
import com.created.team201.presentation.studyManagement.model.StudyMemberUiModel
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudyManagementViewModel(
    private val repository: StudyManagementRepository,
) : ViewModel() {
    private val rounds: MutableLiveData<List<RoundUiModel>> = MutableLiveData()

    private val _studyRounds: MutableLiveData<List<StudyRoundDetailUiModel>> = MutableLiveData()
    val studyRounds: LiveData<List<StudyRoundDetailUiModel>> get() = _studyRounds

    private val _state: MutableLiveData<StudyManagementState> =
        MutableLiveData(StudyManagementState.Member)
    val state: LiveData<StudyManagementState> get() = _state

    private val _currentRound: MutableLiveData<Int> = MutableLiveData(0)
    val currentRound: LiveData<Int> get() = _currentRound

    lateinit var studyInformation: StudyManagementInformationUiModel

    fun fetchStudyInformation(studyId: Long, currentRoundId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getStudyDetail(studyId)
            }.onSuccess {
                studyInformation = it.toUiModel()
                rounds.postValue(it.rounds.map { round: Round -> round.toUiModel() })
                initStudyRounds(studyId, currentRoundId)
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    private fun initStudyRounds(studyId: Long, currentRoundId: Long) {
        _currentRound.postValue(studyInformation.currentRound)
        _state.postValue(StudyManagementState.Member)

        viewModelScope.launch(Dispatchers.IO) {
            getStudyRoundDetail(studyId, currentRoundId)
                .onSuccess {
                    _studyRounds.postValue(listOf(it.toUiModel()))
                    fetchRoundDetail(studyId, PageIndex())
                }.onFailure {
                    Log.e(LOG_ERROR, it.message.toString())
                }
        }
    }

    fun fetchRoundDetail(studyId: Long, pageIndex: PageIndex) {
        val studyRounds = studyRounds.value ?: listOf()
        val rounds = rounds.value ?: listOf()
        updateCurrentPage(pageIndex)

        val index = rounds.indexOf(rounds.find { it.number == currentRound.value })

        viewModelScope.launch {
            if (pageIndex.isFirstPage() && (currentRound.value != rounds.first().number)) {
                getFirstPage(studyId, rounds[index - ONE_PAGE].id, studyRounds)
            }

            if (pageIndex.isLastPage(studyRounds.size) && (currentRound.value != rounds.last().number)) {
                getLastPage(studyId, rounds[index + ONE_PAGE].id, studyRounds)
            }
        }
    }

    private suspend fun getFirstPage(
        studyId: Long,
        roundId: Long,
        studyRounds: List<StudyRoundDetailUiModel>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getStudyRoundDetail(studyId, roundId)
                .onSuccess {
                    _studyRounds.postValue(listOf(it.toUiModel()) + studyRounds)
                }.onFailure {
                    Log.e(LOG_ERROR, it.message.toString())
                }
        }
    }

    private suspend fun getLastPage(
        studyId: Long,
        roundId: Long,
        studyRounds: List<StudyRoundDetailUiModel>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getStudyRoundDetail(studyId, roundId)
                .onSuccess {
                    _studyRounds.postValue(studyRounds + it.toUiModel())
                }.onFailure {
                    Log.e(LOG_ERROR, it.message.toString())
                }
        }
    }

    fun updateCurrentPage(pageIndex: PageIndex) {
        val rounds = rounds.value ?: listOf()
        val studyRounds = studyRounds.value ?: listOf()

        val round = rounds.find {
            it.id == studyRounds[pageIndex.number].id
        } ?: throw IllegalStateException("해당 스터디 없음")

        _currentRound.postValue(round.number)
    }

    private suspend fun getStudyRoundDetail(studyId: Long, roundId: Long): Result<RoundDetail> {
        return runCatching {
            repository.getRoundDetail(studyId, roundId)
        }
    }

    fun updateTodo(currentItemId: Int, todoId: Long, isDone: Boolean, studyId: Long) {
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

        patchTodo(todo, isNecessary, studyId)
    }

    private fun patchTodo(
        todo: TodoUiModel,
        isNecessary: Boolean,
        studyId: Long,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.patchTodo(
                    studyId = studyId,
                    todo = todo.toDomain(),
                    isNecessary = isNecessary,
                )
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
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
        val currentStudyRounds = studyRounds.value ?: listOf()
        val currentStudy = currentStudyRounds[currentPage]

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.createTodo(studyId, CreateTodo(false, currentStudy.id, todoContent))
            }.onSuccess { result ->
                val todoId = result.getOrDefault(DEFAULT_TODO_ID)
                val newOptionalTodos = getNewOptionalTodos(currentStudy, todoId, todoContent)

                val newStudy = currentStudy.copy(optionalTodos = newOptionalTodos)
                val updatedStudyRounds = currentStudyRounds.map { studyRoundDetailUiModel ->
                    studyRoundDetailUiModel.takeIf { it.id != studyId } ?: newStudy
                }
                _studyRounds.value = updatedStudyRounds
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    private fun getNewOptionalTodos(
        currentStudy: StudyRoundDetailUiModel,
        todoId: Long,
        todoContent: String,
    ): MutableList<OptionalTodoUiModel> {
        val newOptionalTodos = currentStudy.optionalTodos.toMutableList()
        newOptionalTodos.removeLast()
        newOptionalTodos.add(
            OptionalTodoUiModel(
                TodoUiModel(todoId, todoContent, false),
                OptionalTodoViewType.DISPLAY.viewType,
            ),
        )
        return newOptionalTodos
    }

    private fun StudyDetail.toUiModel(): StudyManagementInformationUiModel =
        StudyManagementInformationUiModel(
            name = name,
            numberOfCurrentMembers = numberOfCurrentMembers,
            numberOfMaximumMembers = numberOfMaximumMembers,
            role = role,
            startAt = startAt,
            totalRoundCount = totalRoundCount,
            periodOfRound = periodOfRound,
            currentRound = currentRound,
            introduction = introduction,
            rounds = rounds.map { it.toUiModel() },
        )

    private fun RoundDetail.toUiModel(): StudyRoundDetailUiModel = StudyRoundDetailUiModel(
        id = id,
        masterId = masterId,
        role = role,
        necessaryTodo = necessaryTodo.toUiModel(),
        optionalTodos = optionalTodos.map { it.toOptionalTodoUiModel() },
        studyMembers = members.map { it.toUiModel(masterId) },

    )

    private fun Todo.toUiModel(): TodoUiModel = TodoUiModel(
        todoId = todoId,
        content = content,
        isDone = isDone,
    )

    private fun StudyMember.toUiModel(masterId: Long): StudyMemberUiModel = StudyMemberUiModel(
        id = id,
        isMaster = id == masterId,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        isDone = isDone,

    )

    private fun Round.toUiModel(): RoundUiModel = RoundUiModel(id = id, number = number)

    private fun Todo.toOptionalTodoUiModel(): OptionalTodoUiModel = OptionalTodoUiModel(
        todo = this.toUiModel(),
        viewType = OptionalTodoViewType.DISPLAY.viewType,
    )

    companion object {
        private const val DEFAULT_TODO_ID = 0L
        private const val ONE_PAGE = 1
        private const val LOG_ERROR = "ERROR"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = StudyManagementRepositoryImpl(
                    StudyManagementDataSourceImpl(
                        NetworkServiceModule.studyManagementService,
                    ),
                )
                return StudyManagementViewModel(repository) as T
            }
        }
    }
}
