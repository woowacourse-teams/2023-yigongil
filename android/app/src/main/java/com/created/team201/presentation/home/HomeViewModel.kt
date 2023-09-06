package com.created.team201.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.team201.presentation.home.model.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _homeViewState: MutableStateFlow<MutableList<HomeViewState>> = MutableStateFlow(
        mutableListOf()
    )
    val homeViewState: StateFlow<List<HomeViewState>> get() = _homeViewState

    init {
        updateHomeState()
    }

    fun updateHomeState() {
        viewModelScope.launch {
            val fakeData = FakeData()

            _homeViewState.value.addAll(
                listOf(
                    HomeViewState.DashBoard(
                        dDay = fakeData.dashBoard.dDay, mustDo = fakeData.dashBoard.mustDo
                    )
                ) + listOf<HomeViewState.Feed>(
                    HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    ),
                    HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    ),
                    HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    ),
                    HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    ), HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    )
                )
            )
        }
    }


    companion object {
        val data = FakeData()
    }
}

class FakeData {
    val dashBoard = FakeDashBoard()
    val feeds = FakeFeed()
}

class FakeDashBoard {
    val dDay: Int = 13
    val mustDo: String = "라면 끓여먹기"
}

class FakeFeed {
    val userProfile: String = "안녕하세요"
    val userName: String = "안녕하세요"
    val contentImage: String = "안녕하세요"
    val content: String = "안녕하세요반강습니다"
    val publishedDate: String = "2023.12.17"
}


//class HomeViewModel(
//    private val homeRepository: HomeRepository,
//) : ViewModel() {
//    private val _userName: MutableLiveData<String> = MutableLiveData()
//    val userName: LiveData<String> get() = _userName
//
//    private val _userStudies: MutableLiveData<List<StudyUiModel>> = MutableLiveData()
//    val userStudies: LiveData<List<StudyUiModel>> get() = _userStudies
//
//    private val _isStudyExistence: MutableLiveData<Boolean> = MutableLiveData(false)
//    val isStudyExistence: LiveData<Boolean> get() = _isStudyExistence
//
//    fun updateUserStudies() {
//        viewModelScope.launch {
//            runCatching {
//                homeRepository.getUserStudies()
//            }.onSuccess { result ->
//                _userName.value = result.userName
//                _userStudies.value = result.studies.map { it.toUiModel() }.sortedBy { it.leftDays }
//                if (!userStudies.value.isNullOrEmpty()) _isStudyExistence.value = true
//            }.onFailure {
//                Log.d("ERROR", it.toString())
//            }
//        }
//    }
//
//    fun updateNecessaryTodo(todo2: TodoWithRoundIdUiModel, isDone: Boolean) {
//        updateNecessaryTodoCheck(todo2.todo2Id, isDone)
//
//        viewModelScope.launch {
//            homeRepository.patchNecessaryTodo(todo2.roundId, isDone)
//                .onSuccess { }
//                .onFailure { }
//        }
//    }
//
//    fun updateOptionalTodo(todo2: TodoWithRoundIdUiModel, isDone: Boolean) {
//        updateOptionalTodoCheck(todo2.todo2Id, isDone)
//
//        viewModelScope.launch {
//            homeRepository.patchOptionalTodo(todo2.toDomain(isDone), todo2.roundId)
//                .onSuccess { }
//                .onFailure { }
//        }
//    }
//
//    private fun updateNecessaryTodoCheck(id: Long, isDone: Boolean) {
//        _userStudies.value = userStudies.value?.map { studyUiModel ->
//            studyUiModel.takeIf { it.necessaryTodo.todo2Id != id } ?: studyUiModel.copy(
//                necessaryTodo = studyUiModel.necessaryTodo.copy(isDone = isDone),
//            )
//        }
//    }
//
//    private fun updateOptionalTodoCheck(id: Long, isDone: Boolean) {
//        _userStudies.value = userStudies.value?.map { studyUiModel ->
//            studyUiModel.takeIf { todo2UiModel -> !todo2UiModel.optionalTodos.any { it.todo2Id == id } }
//                ?: studyUiModel.copy(
//                    optionalTodos = studyUiModel.optionalTodos.map {
//                        it.takeUnless { it.todo2Id == id } ?: it.copy(isDone = isDone)
//                    },
//                )
//        }
//    }
//
//    private fun Study.toUiModel(): StudyUiModel =
//        StudyUiModel(
//            studyId = studyId.toLong(),
//            studyName = studyName,
//            progressRate = progressRate,
//            leftDays = leftDays,
//            nextDate = nextDate,
//            necessaryTodo = necessaryTodo.toUiModel(roundId),
//            optionalTodos = optionalTodo.map { it.toUiModel(roundId) },
//        )
//
//    private fun Todo2.toUiModel(roundId: Int): TodoWithRoundIdUiModel = TodoWithRoundIdUiModel(
//        todo2Id = todo2Id,
//        content = content ?: "",
//        isDone = isDone,
//        roundId = roundId,
//    )
//
//    private fun TodoWithRoundIdUiModel.toDomain(isDone: Boolean): Todo2 = Todo2(
//        todo2Id = todo2Id,
//        content = content,
//        isDone = isDone,
//
//        )
//
//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                HomeViewModel(
//                    homeRepository = HomeRepositoryImpl(
//                        HomeDataSourceImpl(NetworkServiceModule.homeService),
//                    ),
//                )
//            }
//        }
//    }
//}