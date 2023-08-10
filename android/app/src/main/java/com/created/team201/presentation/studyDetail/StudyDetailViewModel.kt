package com.created.team201.presentation.studyDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Member
import com.created.domain.model.Role
import com.created.domain.model.StudyDetail
import com.created.domain.repository.StudyDetailRepository
import com.created.team201.data.datasource.remote.StudyDetailDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.StudyDetailRepositoryImpl
import com.created.team201.presentation.studyDetail.model.StudyDetailUIModel
import com.created.team201.presentation.studyDetail.model.StudyMemberUIModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import kotlinx.coroutines.launch

class StudyDetailViewModel private constructor(
    private val studyDetailRepository: StudyDetailRepository,
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

    private val _isStartStudy: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)
    val isStartStudy: NonNullLiveData<Boolean> get() = _isStartStudy

    private val _isFullMember: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)
    val isFullMember: NonNullLiveData<Boolean> get() = _isFullMember

    private val _canStudyStart: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)
    val canStudyStart: NonNullLiveData<Boolean> get() = _canStudyStart

    private val _studyMemberCount: NonNullMutableLiveData<Int> = NonNullMutableLiveData(0)
    val studyMemberCount: NonNullLiveData<Int> get() = _studyMemberCount

    fun fetchStudyDetail(studyId: Long, notifyInvalidStudy: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.getStudyDetail(studyId).toUIModel()
            }.onSuccess {
                _study.value = it
                _studyParticipants.value = it.studyMembers
                _isFullMember.value = it.peopleCount == it.memberCount
                _state.value = it.role.toStudyDetailState(it.canStartStudy)
                _studyMemberCount.value = it.memberCount
                _canStudyStart.value = it.canStartStudy
                if (it.role == Role.MASTER) fetchApplicants(studyId)
            }.onFailure {
                notifyInvalidStudy()
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
                            it.toUIModel(
                                study.value.studyMasterId,
                                true,
                            )
                        },
                    )
            }
        }
    }

    fun startStudy(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                studyDetailRepository.startStudy(studyId)
            }.onSuccess {
                _isStartStudy.value = true
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

    private fun StudyDetail.toUIModel(): StudyDetailUIModel = StudyDetailUIModel(
        studyMasterId = studyMasterId,
        isMaster = role == Role.MASTER,
        title = this.name,
        introduction = this.introduction,
        peopleCount = this.numberOfMaximumMembers,
        role = this.role,
        startDate = this.startAt,
        period = this.totalRoundCount.toString(),
        cycle = this.periodOfRound,
        memberCount = this.members.size,
        canStartStudy = StudyDetail.canStartStudy(this.numberOfCurrentMembers),
        studyMembers = this.members.map { it.toUIModel(this.studyMasterId, isApplicant = false) },
    )

    private fun Member.toUIModel(studyMasterId: Long, isApplicant: Boolean): StudyMemberUIModel =
        StudyMemberUIModel(
            id = id,
            isMaster = this.id == studyMasterId,
            isApplicant = isApplicant,
            profileImageUrl = this.profileImage,
            name = this.nickname,
            successRate = this.successRate.toInt(),
            tier = this.tier,
        )

    private fun Role.toStudyDetailState(canStartStudy: Boolean): StudyDetailState = when (this) {
        Role.MASTER -> StudyDetailState.Master(canStartStudy)
        Role.MEMBER -> StudyDetailState.Member
        Role.APPLICANT -> StudyDetailState.Applicant
        Role.NOTHING -> StudyDetailState.Nothing(isFullMember.value)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = StudyDetailRepositoryImpl(
                    StudyDetailDataSourceImpl(
                        NetworkServiceModule.studyDetailService,
                    ),
                )
                return StudyDetailViewModel(repository) as T
            }
        }
    }
}
