package com.created.team201.presentation.studyThread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Feeds
import com.created.domain.model.MustDoCertification
import com.created.domain.model.MustDoStatus
import com.created.domain.model.Role
import com.created.domain.model.WeeklyMustDo
import com.created.domain.repository.MyPageRepository
import com.created.domain.repository.StudyDetailRepository
import com.created.domain.repository.ThreadRepository
import com.created.team201.presentation.common.customview.dayofselector.DayOfWeek
import com.created.team201.presentation.studyDetail.model.StudyDetailUIModel
import com.created.team201.presentation.studyThread.model.WeeklyMustDoUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val threadRepository: ThreadRepository,
    private val detailRepository: StudyDetailRepository,
    private val myPageRepository: MyPageRepository,
) : ViewModel() {

    private var myId: Long = DEFAULT_MY_ID
    private var studyId: Long = DEFAULT_STUDY_ID

    private val _uiState: MutableStateFlow<ThreadUiState> = MutableStateFlow(ThreadUiState.Loading)
    val uiState: StateFlow<ThreadUiState> get() = _uiState.asStateFlow()

    private val _weekNumber: NonNullMutableLiveData<Int> =
        NonNullMutableLiveData(DEFAULT_WEEK_NUMBER)
    val weekNumber: NonNullLiveData<Int> get() = _weekNumber

    private val _currentWeeklyMustDos: MutableLiveData<List<WeeklyMustDoUiModel>> =
        MutableLiveData()

    val currentWeeklyStudyDays: LiveData<List<DayOfWeek>> = _currentWeeklyMustDos.map {
        it.map { weeklyMustDoUiModel ->
            weeklyMustDoUiModel.dayOfWeek
        }
    }

    private val _upcomingStudyDay: MutableLiveData<DayOfWeek> = MutableLiveData()
    val upcomingStudyDay: LiveData<DayOfWeek> get() = _upcomingStudyDay

    private var currentRoundId: Long = DEFAULT_CURRENT_ROUND_ID

    private val _mustDoContent: MutableLiveData<String> = MutableLiveData()
    val mustDoContent: LiveData<String> get() = _mustDoContent

    private var currentWeekNumber: Int = DEFAULT_CURRENT_WEEK_NUMBER

    lateinit var studyDetail: StudyDetailUIModel

    fun initStudyThread(studyId: Long) {
        this.studyId = studyId

        getMyProfile()
        updateMustDoCertification()
        updateFeeds()
    }

    fun dispatchFeed(message: String) {
        viewModelScope.launch {
            runCatching { threadRepository.updateFeeds(studyId, message, null) }
                .onFailure {
                }
        }
    }

    private fun updateMustDoCertification() {
        viewModelScope.launch {
            runCatching { threadRepository.getMustDoCertification(studyId) }
                .onSuccess { mustDoCertification ->
                    currentWeekNumber = mustDoCertification.upcomingRound.weekNumber
                    _weekNumber.value = mustDoCertification.upcomingRound.weekNumber
                    updateMustDoCertificationInState(mustDoCertification)
                    updateWeeklyMustDos(studyId, weekNumber.value)
                }
        }
    }

    private fun updateMustDoCertificationInState(certification: MustDoCertification) {
        if (uiState.value is ThreadUiState.Success) {
            _uiState.value =
                (uiState.value as ThreadUiState.Success).copy(
                    studyName = certification.studyName,
                    mustDo = listOf(certification.me) + certification.others,
                )
        } else {
            _uiState.value = ThreadUiState.Success(
                studyName = certification.studyName,
                mustDo = listOf(certification.me) + certification.others,
            )
        }
    }

    private fun updateFeeds() {
        viewModelScope.launch {
            threadRepository.getFeeds(studyId).collectLatest {
                updateFeedsInState(it)
            }
        }
    }

    private fun updateFeedsInState(feeds: List<Feeds>) {
        if (uiState.value is ThreadUiState.Success) {
            _uiState.value =
                (uiState.value as ThreadUiState.Success).copy(
                    feeds = feeds,
                )
        } else {
            _uiState.value = ThreadUiState.Success(
                feeds = feeds,
            )
        }
    }

    private fun updateWeeklyMustDos(studyId: Long, weekNumber: Int) {
        viewModelScope.launch {
            runCatching {
                threadRepository.getWeeklyMustDo(studyId, weekNumber)
            }.onSuccess { weeklyMustDos ->
                _currentWeeklyMustDos.value = weeklyMustDos.map { it.toUiModel() }
                _upcomingStudyDay.value = weeklyMustDos.map { it.toUiModel() }.firstOrNull {
                    it.status == MustDoStatus.IN_PROGRESS
                }?.dayOfWeek ?: weeklyMustDos.map { it.toUiModel() }.last().dayOfWeek

                updateMustDoContent(
                    _upcomingStudyDay.value ?: weeklyMustDos.map { it.toUiModel() }
                        .last().dayOfWeek,
                )
            }
        }
    }

    fun updatePreviousRound() {
        if (weekNumber.value == FIRST_PAGE) {
            return
        }
        _weekNumber.value = weekNumber.value - ONE_PAGE
        updateWeeklyMustDos(studyId, weekNumber.value)
    }

    fun updateNextRound() {
        if (weekNumber.value > currentWeekNumber) {
            return
        }
        _weekNumber.value = weekNumber.value + ONE_PAGE
        updateWeeklyMustDos(studyId, weekNumber.value)
    }

    fun updateMustDoContent(dayOfWeek: DayOfWeek) {
        _mustDoContent.value =
            _currentWeeklyMustDos.value?.first { it.dayOfWeek == dayOfWeek }?.mustDo ?: ""
        currentRoundId = _currentWeeklyMustDos.value?.first { it.dayOfWeek == dayOfWeek }?.id
            ?: DEFAULT_CURRENT_ROUND_ID
    }

    fun updateMustDo(mustDoContent: String) {
        viewModelScope.launch {
            runCatching {
                threadRepository.putMustDo(currentRoundId, mustDoContent)
            }.onSuccess {
                updateWeeklyMustDos(studyId, weekNumber.value)
            }
        }
    }

    private fun getMyProfile() {
        viewModelScope.launch {
            myPageRepository.getMyPage()
                .onSuccess {
                    myId = it.id
                }

            runCatching {
                detailRepository.getStudyDetail(studyId)
            }.onSuccess {
                val myRole: Role = if (it.studyMasterId == myId) Role.MASTER else Role.STUDY_MEMBER
                studyDetail = StudyDetailUIModel.createFromStudyDetailRole(it, myRole)
            }
        }
    }

    private fun WeeklyMustDo.toUiModel(): WeeklyMustDoUiModel = WeeklyMustDoUiModel(
        id = id,
        mustDo = mustDo,
        dayOfWeek = DayOfWeek.of(dayOfWeek),
        status = status,
    )

    fun isMyProfile(memberId: Long): Boolean = (memberId == myId)

    fun endStudy(notifyCantEndStudy: () -> Unit) {
        if (studyDetail.minimumWeeks >= currentWeekNumber) {
            notifyCantEndStudy()
            return
        }

        viewModelScope.launch {
            threadRepository.endStudy(studyId)
        }
    }

    fun quitStudy() {
        viewModelScope.launch {
            threadRepository.quitStudy(studyId)
        }
    }

    companion object {
        private const val DEFAULT_STUDY_ID = 0L
        private const val DEFAULT_MY_ID = -1L
        private const val DEFAULT_CURRENT_ROUND_ID = -1L
        private const val DEFAULT_CURRENT_WEEK_NUMBER = -1
        private const val FIRST_PAGE = 1
        private const val ONE_PAGE = 1
        private const val DEFAULT_WEEK_NUMBER = 1
    }
}
