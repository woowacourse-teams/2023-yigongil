package com.created.team201.presentation.studyManagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.created.domain.model.CreateTodo
import com.created.domain.model.Nickname
import com.created.domain.model.PageIndex
import com.created.domain.model.PeriodUnit
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
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
import com.created.team201.presentation.myPage.model.ProfileInformationUiModel
import com.created.team201.presentation.myPage.model.ProfileUiModel
import com.created.team201.presentation.onBoarding.model.NicknameUiModel
import com.created.team201.presentation.studyList.model.PeriodUiModel
import com.created.team201.presentation.studyManagement.TodoState.DEFAULT
import com.created.team201.presentation.studyManagement.adapter.OptionalTodoViewType.DISPLAY
import com.created.team201.presentation.studyManagement.adapter.OptionalTodoViewType.EDIT
import com.created.team201.presentation.studyManagement.model.NecessaryTodoUiModel
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel
import com.created.team201.presentation.studyManagement.model.RoundUiModel
import com.created.team201.presentation.studyManagement.model.StudyManagementInformationUiModel
import com.created.team201.presentation.studyManagement.model.StudyMemberUiModel
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudyManagementViewModel(
    private val repository: StudyManagementRepository,
) : ViewModel() {

    private val rounds: MutableLiveData<List<RoundUiModel>> = MutableLiveData()

    private val _studyRounds: MutableLiveData<List<StudyRoundDetailUiModel>> = MutableLiveData()
    val studyRounds: LiveData<List<StudyRoundDetailUiModel>> get() = _studyRounds

    private val _state: MutableLiveData<StudyManagementState> =
        MutableLiveData(StudyManagementState.Member)
    val state: LiveData<StudyManagementState> get() = _state

    private val _currentRound: NonNullMutableLiveData<Int> = NonNullMutableLiveData(0)
    val currentRound: NonNullLiveData<Int> get() = _currentRound

    lateinit var studyInformation: StudyManagementInformationUiModel

    lateinit var myProfile: ProfileUiModel

    private val _isStudyRoundsLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    val isStudyRoundsLoaded: LiveData<Boolean> get() = _isStudyRoundsLoaded

    private val _todoState: MutableLiveData<TodoState> = MutableLiveData(DEFAULT)
    val todoState: LiveData<TodoState> get() = _todoState

    private val currentStudyRounds get() = studyRounds.value ?: listOf()
    private val studyDetails get() = studyRounds.value ?: listOf()
    val currentRoundDetail get() = studyDetails[currentRound.value - CONVERT_PAGE_TO_ROUND]

    fun initStudyManagement(studyId: Long) {
        viewModelScope.launch {
            getMyProfile()
            fetchStudyInformation(studyId)
            initStudyRounds(studyId)
            _isStudyRoundsLoaded.value = true
        }
    }

    private fun getMyProfile() {
        viewModelScope.launch {
            runCatching {
                repository.getMyProfile()
            }.onSuccess { profile ->
                myProfile = profile.toUiModel()
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    private suspend fun fetchStudyInformation(studyId: Long) {
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            runCatching {
                repository.getStudyDetail(studyId)
            }.onSuccess {
                studyInformation = it.toUiModel()
                rounds.postValue(it.rounds.map { round: Round -> round.toUiModel() })
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    fun initStatus() {
        _state.value = StudyManagementState.getRoleStatus(studyInformation.role)
    }

    private suspend fun initStudyRounds(studyId: Long) {
        val rounds = rounds.value ?: listOf()
        _currentRound.postValue(studyInformation.currentRound)

        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            for (round in rounds) {
                getStudyRoundDetail(studyId, round.id)
                    .onSuccess {
                        val studyRounds = studyRounds.value ?: listOf()
                        _studyRounds.postValue(studyRounds + it.toUiModel())
                    }.onFailure {
                        Log.e(LOG_ERROR, it.message.toString())
                    }
            }
        }
    }

    private suspend fun getStudyRoundDetail(studyId: Long, roundId: Long): Result<RoundDetail> {
        return runCatching {
            repository.getRoundDetail(studyId, roundId)
        }
    }

    fun updateCurrentPage(pageIndex: PageIndex) {
        _currentRound.value = pageIndex.toRound()
    }

    fun addTodo(isNecessary: Boolean, content: String) {
        when (isNecessary) {
            true -> addNecessaryTodo(content)
            false -> addOptionalTodo(content)
        }
        _todoState.value = DEFAULT
    }

    private fun addNecessaryTodo(todoContent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.createNecessaryTodo(currentRoundDetail.id, CreateTodo(todoContent))
            }.onSuccess { result ->
                val newNecessaryTodo = currentRoundDetail.necessaryTodo.copy(
                    todo = currentRoundDetail.necessaryTodo.todo.copy(content = todoContent),
                    isInitialized = true,
                )
                val newRound = currentRoundDetail.copy(necessaryTodo = newNecessaryTodo)
                val updatedStudyRounds = currentStudyRounds.map { studyRoundDetailUiModel ->
                    studyRoundDetailUiModel.takeIf { it.id != currentRoundDetail.id } ?: newRound
                }
                _studyRounds.postValue(updatedStudyRounds)
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    private fun addOptionalTodo(todoContent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.createOptionalTodo(currentRoundDetail.id, CreateTodo(todoContent))
            }.onSuccess { result ->
                val todoId = result.getOrDefault(DEFAULT_TODO_ID)
                val newOptionalTodos = getNewOptionalTodos(currentRoundDetail, todoId, todoContent)
                val newRound = currentRoundDetail.copy(optionalTodos = newOptionalTodos)
                val updatedStudyRounds = currentStudyRounds.map { studyRoundDetailUiModel ->
                    studyRoundDetailUiModel.takeIf { it.id != currentRoundDetail.id } ?: newRound
                }
                _studyRounds.postValue(updatedStudyRounds)
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    private fun getNewOptionalTodos(
        currentStudy: StudyRoundDetailUiModel,
        todoId: Long,
        todoContent: String,
    ): List<OptionalTodoUiModel> {
        val newOptionalTodos = currentStudy.optionalTodos.map {
            it.copy(viewType = DISPLAY.viewType)
        }.toMutableList()
        newOptionalTodos.add(
            OptionalTodoUiModel(
                TodoUiModel(todoId, todoContent, false),
                DISPLAY.viewType,
            ),
        )
        return newOptionalTodos.toList()
    }

    fun updateTodoIsDone(isNecessary: Boolean, todoId: Long, isDone: Boolean) {
        when (isNecessary) {
            true -> updateNecessaryTodoIsDone(isDone)
            false -> updateOptionalTodoIsDone(todoId, isDone)
        }
    }

    private fun updateNecessaryTodoIsDone(isDone: Boolean) {
        val newNecessaryTodo = currentRoundDetail.necessaryTodo.copy(
            todo = currentRoundDetail.necessaryTodo.todo.copy(isDone = isDone),
            isInitialized = true,
        )

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.patchNecessaryTodoIsDone(
                    currentRoundDetail.id,
                    newNecessaryTodo.todo.toDomain(),
                )
            }.onSuccess {
                val newRound = currentRoundDetail.copy(necessaryTodo = newNecessaryTodo)
                val updatedStudyRounds = currentStudyRounds.map { studyRoundDetailUiModel ->
                    studyRoundDetailUiModel.takeIf { it.id != currentRoundDetail.id } ?: newRound
                }
                _studyRounds.postValue(updatedStudyRounds)
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    private fun updateOptionalTodoIsDone(todoId: Long, isDone: Boolean) {
        val newOptionalTodo = getNewOptionalTodo(todoId, isDone)
        viewModelScope.launch(Dispatchers.IO) {
            val optionalTodos = currentRoundDetail.optionalTodos.map { optionalTodo ->
                optionalTodo.takeIf { it.todo.todoId != todoId } ?: newOptionalTodo
            }
            runCatching {
                repository.patchOptionalTodo(
                    currentRoundDetail.id,
                    newOptionalTodo.todo.toDomain(),
                )
            }.onSuccess {
                val newRound = currentRoundDetail.copy(optionalTodos = optionalTodos)
                val updatedStudyRounds = currentStudyRounds.map { studyRoundDetailUiModel ->
                    studyRoundDetailUiModel.takeIf { it.id != currentRoundDetail.id } ?: newRound
                }
                _studyRounds.postValue(updatedStudyRounds)
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    private fun getNewOptionalTodo(todoId: Long, isDone: Boolean): OptionalTodoUiModel {
        val currentOptionalTodo = currentRoundDetail.optionalTodos.find { optionalTodo ->
            optionalTodo.todo.todoId == todoId
        } ?: OptionalTodoUiModel(
            TodoUiModel(DEFAULT_TODO_ID, "", false),
            DISPLAY.viewType,
        )
        return currentOptionalTodo.copy(todo = currentOptionalTodo.todo.copy(isDone = isDone))
    }

    fun updateNecessaryTodoContent(todoContent: String) {
        val newNecessaryTodo = currentRoundDetail.necessaryTodo.copy(
            todo = currentRoundDetail.necessaryTodo.todo.copy(content = todoContent),
        )

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.patchNecessaryTodo(
                    currentRoundDetail.id,
                    newNecessaryTodo.todo.toDomain(),
                )
            }.onSuccess {
                val newRound = currentRoundDetail.copy(necessaryTodo = newNecessaryTodo)
                val updatedStudyRounds = currentStudyRounds.map { studyRoundDetailUiModel ->
                    studyRoundDetailUiModel.takeIf { it.id != currentRoundDetail.id } ?: newRound
                }
                _studyRounds.postValue(updatedStudyRounds)
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    fun updateOptionalTodosContent(updatedTodos: List<OptionalTodoUiModel>) {
        val newOptionalTodos = currentRoundDetail.optionalTodos.map { optionalTodo ->
            updatedTodos.find { it.todo.todoId == optionalTodo.todo.todoId } ?: optionalTodo
        }.map {
            it.copy(viewType = DISPLAY.viewType)
        }

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                for (updatedTodo in updatedTodos) {
                    repository.patchOptionalTodo(currentRoundDetail.id, updatedTodo.todo.toDomain())
                }
            }.onSuccess {
                val newRound = currentRoundDetail.copy(optionalTodos = newOptionalTodos)
                val updatedStudyRounds = currentStudyRounds.map { studyRoundDetailUiModel ->
                    studyRoundDetailUiModel.takeIf { it.id != currentRoundDetail.id } ?: newRound
                }
                _studyRounds.postValue(updatedStudyRounds)
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    fun deleteTodo(optionalTodo: OptionalTodoUiModel) {
        val newOptionalTodos = currentRoundDetail.optionalTodos.toMutableList()
        newOptionalTodos.removeIf {
            it.todo.todoId == optionalTodo.todo.todoId
        }

        viewModelScope.launch {
            kotlin.runCatching {
                repository.deleteOptionalTodo(currentRoundDetail.id, optionalTodo.todo.todoId)
            }.onSuccess {
                val newRound = currentRoundDetail.copy(
                    optionalTodos = newOptionalTodos.map {
                        it.copy(viewType = EDIT.viewType)
                    },
                )
                val updatedStudyRounds = currentStudyRounds.map { studyRoundDetailUiModel ->
                    studyRoundDetailUiModel.takeIf { it.id != currentRoundDetail.id } ?: newRound
                }
                _studyRounds.postValue(updatedStudyRounds)
            }.onFailure {
                Log.e(LOG_ERROR, it.message.toString())
            }
        }
    }

    fun setTodoState(updatedTodoState: TodoState) {
        _todoState.value = updatedTodoState
    }

    private fun StudyDetail.toUiModel(): StudyManagementInformationUiModel =
        StudyManagementInformationUiModel(
            name = name,
            numberOfCurrentMembers = numberOfCurrentMembers,
            numberOfMaximumMembers = numberOfMaximumMembers,
            role = role,
            startAt = startAt,
            totalRoundCount = totalRoundCount,
            periodOfRound = periodOfRound.toPeriodUiModel(),
            currentRound = currentRound,
            introduction = introduction,
            rounds = rounds.map { it.toUiModel() },
        )

    private fun RoundDetail.toUiModel(): StudyRoundDetailUiModel = StudyRoundDetailUiModel(
        id = id,
        masterId = masterId,
        role = role,
        necessaryTodo = necessaryTodo.toNecessaryTodoUiModel(),
        optionalTodos = optionalTodos.map { it.toOptionalTodoUiModel() },
        studyMembers = members.map { it.toUiModel(masterId) },

    )

    private fun String.toPeriodUiModel(): PeriodUiModel =
        PeriodUiModel(Character.getNumericValue(first()), PeriodUnit.valueOf(last()))

    private fun Todo.toUiModel(): TodoUiModel = TodoUiModel(
        todoId = todoId,
        content = if (content == null) "" else content,
        isDone = isDone,
    )

    private fun StudyMember.toUiModel(masterId: Long): StudyMemberUiModel = StudyMemberUiModel(
        id = id,
        isMaster = id == masterId,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        isDone = isDone,
        isDeleted = isDeleted,
    )

    private fun Round.toUiModel(): RoundUiModel = RoundUiModel(id = id, number = number)

    private fun Todo.toOptionalTodoUiModel(): OptionalTodoUiModel = OptionalTodoUiModel(
        todo = this.toUiModel(),
        viewType = DISPLAY.viewType,
    )

    private fun Todo.toNecessaryTodoUiModel(): NecessaryTodoUiModel = NecessaryTodoUiModel(
        todo = this.toUiModel(),
        isInitialized = content != null,
    )

    private fun Profile.toUiModel(): ProfileUiModel = ProfileUiModel(
        githubId,
        id,
        profileInformation.toUiModel(),
        profileImageUrl,
        successRate,
        successfulRoundCount,
        tier,
        tierProgress,
    )

    private fun ProfileInformation.toUiModel(): ProfileInformationUiModel =
        ProfileInformationUiModel(
            nickname = nickname.toUiModel(),
            introduction = introduction
        )

    private fun Nickname.toUiModel(): NicknameUiModel = NicknameUiModel(
        nickname = nickname
    )

    companion object {
        private const val DEFAULT_TODO_ID = 0L
        private const val CONVERT_PAGE_TO_ROUND = 1
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
