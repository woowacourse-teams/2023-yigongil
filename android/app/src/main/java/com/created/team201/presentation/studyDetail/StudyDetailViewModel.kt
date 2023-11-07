package com.created.team201.presentation.studyDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Nickname
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
import com.created.domain.model.Role
import com.created.domain.model.StudyDetail
import com.created.domain.model.StudyStart
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.repository.GuestRepository
import com.created.team201.data.repository.StudyDetailRepository
import com.created.team201.presentation.common.customview.dayofselector.DayOfWeek
import com.created.team201.presentation.myPage.model.ProfileInformationUiModel
import com.created.team201.presentation.myPage.model.ProfileUiModel
import com.created.team201.presentation.onBoarding.model.NicknameUiModel
import com.created.team201.presentation.studyDetail.model.StudyDetailUIModel
import com.created.team201.presentation.studyDetail.model.StudyMemberUIModel
import com.created.team201.presentation.studyDetail.model.StudyMemberUIModel.Companion.toUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyDetailViewModel @Inject constructor(
    private val studyDetailRepository: StudyDetailRepository,
    private val guestRepository: GuestRepository,
) : ViewModel() {

    private val _study: NonNullMutableLiveData<StudyDetailUIModel> =
        NonNullMutableLiveData(StudyDetailUIModel.INVALID_STUDY_DETAIL)
    val study: NonNullLiveData<StudyDetailUIModel> get() = _study

    private val _studyParticipants: NonNullMutableLiveData<List<StudyMemberUIModel>> =
        NonNullMutableLiveData(listOf())
    val studyParticipants: NonNullLiveData<List<StudyMemberUIModel>> get() = _studyParticipants

    private val _state: NonNullMutableLiveData<StudyDetailState> =
        NonNullMutableLiveData(StudyDetailState.Nothing(true))
    val state: LiveData<StudyDetailState> get() = _state

    private val _startStudyState: NonNullMutableLiveData<UIState> =
        NonNullMutableLiveData(UIState.Loading)
    val startStudyState: NonNullLiveData<UIState> get() = _startStudyState

    private val _isFullMember: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)
    val isFullMember: NonNullLiveData<Boolean> get() = _isFullMember

    private val _canStudyStart: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)
    val canStudyStart: NonNullLiveData<Boolean> get() = _canStudyStart

    private val _studyMemberCount: NonNullMutableLiveData<Int> = NonNullMutableLiveData(0)
    val studyMemberCount: NonNullLiveData<Int> get() = _studyMemberCount
    lateinit var myProfile: ProfileUiModel

    private val isGuest: Boolean get() = guestRepository.getIsGuest()

    fun refresh(studyId: Long) {
        viewModelScope.launch {
            getMyProfile()
            studyDetailRepository.getStudyMemberRole(studyId)
                .onSuccess {
                    val role = Role.valueOf(it)
                    _study.value = study.value.copy(role = role)
                    _state.value = role.toStudyDetailState(study.value.canStartStudy)
                }
        }
    }

    fun fetchStudyDetail(studyId: Long, notifyInvalidStudy: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.getStudyDetail(studyId)
            }.onSuccess { studyDetail ->
                when (isGuest) {
                    true -> {
                        val studyDetailUIModel =
                            StudyDetailUIModel.createFromStudyDetailRole(
                                studyDetail = studyDetail,
                                role = Role.GUEST,
                            )
                        setStudyDetail(studyDetailUIModel, studyId)
                    }

                    false -> {
                        getMyProfile()
                        studyDetailRepository.getStudyMemberRole(studyId)
                            .onSuccess { role ->
                                val studyDetailUIModel =
                                    StudyDetailUIModel.createFromStudyDetailRole(
                                        studyDetail = studyDetail,
                                        role = Role.valueOf(role),
                                    )
                                setStudyDetail(studyDetailUIModel, studyId)
                            }.onFailure {
                                notifyInvalidStudy()
                            }
                    }
                }
            }.onFailure {
                notifyInvalidStudy()
            }
        }
    }

    private fun setStudyDetail(studyDetailUIModel: StudyDetailUIModel, studyId: Long) {
        with(studyDetailUIModel) {
            _study.value = this
            _studyParticipants.value = studyMembers
            _isFullMember.value = peopleCount == memberCount
            _state.value = this.role.toStudyDetailState(canStartStudy)
            _studyMemberCount.value = memberCount
            _canStudyStart.value = canStartStudy
            if (this.role == Role.MASTER) fetchApplicants(studyId)
        }
    }

    private fun getMyProfile() {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.getMyProfile()
            }.onSuccess { profile ->
                myProfile = profile.toUiModel()
            }.onFailure {
            }
        }
    }

    fun participateStudy(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.participateStudy(studyId)
            }.onFailure { // 204 No Content가 onFailure로 가는 현상이 있습니다.
                _state.value = StudyDetailState.Applicant
            }
        }
    }

    private fun fetchApplicants(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.getStudyApplicants(studyId)
            }.onSuccess { members ->
                _studyParticipants.value =
                    _studyParticipants.value.plus(
                        members.map {
                            it.toUiModel(
                                study.value.studyMasterId,
                                true,
                            )
                        },
                    )
            }
        }
    }

    fun startStudy(studyId: Long, days: List<DayOfWeek>) {
        viewModelScope.launch {
            runCatching {
                val studyStart = StudyStart(days.map { dayOfWeek -> dayOfWeek.toDomain() })
                studyDetailRepository.startStudy(studyId, studyStart)
            }.onSuccess {
                _startStudyState.value = UIState.Success
            }.onFailure {
                _startStudyState.value = UIState.Fail
            }
        }
    }

    fun acceptApplicant(studyId: Long, memberId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.acceptApplicant(studyId, memberId)
            }.onFailure { // 204 No Content가 onFailure로 가는 현상이 있습니다.
                val studyParticipants = _studyParticipants.value
                val acceptedMember =
                    studyParticipants.find { it.id == memberId } ?: return@launch
                _studyParticipants.value =
                    studyParticipants.minus(acceptedMember) + acceptedMember.copy(isApplicant = false)
                _canStudyStart.value = StudyDetail.canStartStudy(studyParticipants.size)
                _studyMemberCount.value = _studyMemberCount.value.plus(1)
                if (study.value.peopleCount == studyMemberCount.value) _isFullMember.value = true
            }
        }
    }

    private fun Role.toStudyDetailState(canStartStudy: Boolean): StudyDetailState = when (this) {
        Role.MASTER -> StudyDetailState.Master(canStartStudy)
        Role.STUDY_MEMBER -> StudyDetailState.Member
        Role.APPLICANT -> StudyDetailState.Applicant
        Role.NOTHING -> StudyDetailState.Nothing(isFullMember.value)
        Role.GUEST -> StudyDetailState.Guest(isFullMember.value)
    }

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
            introduction = introduction,
        )

    private fun Nickname.toUiModel(): NicknameUiModel = NicknameUiModel(
        nickname = nickname,
    )

    sealed interface UIState {
        object Success : UIState
        object Loading : UIState
        object Fail : UIState
        object Idle : UIState
    }
}
