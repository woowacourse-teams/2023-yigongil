package com.created.team201.presentation.updateStudy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Period
import com.created.domain.model.PeriodUnit
import com.created.domain.model.StudyDetail.Companion.getPeriod
import com.created.domain.model.UpdateStudy
import com.created.domain.repository.StudyDetailRepository
import com.created.domain.repository.UpdateStudyRepository
import com.created.team201.presentation.updateStudy.model.PeriodUiModel
import com.created.team201.presentation.updateStudy.model.UpdateStudyUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import com.created.team201.util.addSourceList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateStudyViewModel @Inject constructor(
    private val updateStudyRepository: UpdateStudyRepository,
    private val studyDetailRepository: StudyDetailRepository,
) : ViewModel() {
    private val _name: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val name: NonNullLiveData<String>
        get() = _name

    private val _introduction: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val introduction: NonNullLiveData<String>
        get() = _introduction

    private val _peopleCount: NonNullMutableLiveData<Int> = NonNullMutableLiveData(0)
    val peopleCount: NonNullLiveData<Int>
        get() = _peopleCount

    private val _startDate: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val startDate: NonNullLiveData<String>
        get() = _startDate

    private val _period: NonNullMutableLiveData<Int> = NonNullMutableLiveData(0)
    val period: NonNullLiveData<Int>
        get() = _period

    private val _cycle: NonNullMutableLiveData<PeriodUiModel> =
        NonNullMutableLiveData(PeriodUiModel(0, PeriodUnit.DAY))
    val cycle: NonNullLiveData<PeriodUiModel>
        get() = _cycle

    private val _content: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val content: NonNullLiveData<String>
        get() = _content

    private val _isEnableCreateStudy: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            addSourceList(name, introduction, peopleCount, startDate, period, cycle) {
                isInitializeCreateStudyInformation()
            }
        }
    val isEnableCreateStudy: LiveData<Boolean>
        get() = _isEnableCreateStudy

    private val _studyState: MutableLiveData<State> = MutableLiveData()
    val studyState: LiveData<State>
        get() = _studyState

    private var isOpenStudy: Boolean = false

    fun setViewState(studyId: Long) {
        viewModelScope.launch {
            runCatching { studyDetailRepository.getStudyDetail(studyId) }
                .onSuccess {
                    _name.value = it.name
                    _content.value = it.introduction
                    _peopleCount.value = it.numberOfMaximumMembers
                    _startDate.value = it.startAt
                    _cycle.value = cycle.value.copy(date = getPeriod(it.periodOfRound))
                    _period.value = it.totalRoundCount
                }
        }
    }

    fun setName(name: String) {
        _name.value = name.replace("\n", "")
    }

    fun setIntroduction(introduction: String) {
        _introduction.value = introduction
    }

    fun setPeopleCount(peopleCount: Int) {
        _peopleCount.value = peopleCount
    }

    fun setStartDate(startDate: String) {
        _startDate.value = startDate
    }

    fun setCycle(date: Int, type: Int) {
        val cycle = PeriodUiModel(date, PeriodUnit.valueOf(type))
        _cycle.value = cycle
    }

    fun setPeriod(period: Int) {
        _period.value = period
    }

    fun editStudy(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                UpdateStudyUiModel(
                    name.value.trim(),
                    peopleCount.value,
                    startDate.value,
                    period.value,
                    cycle.value,
                    introduction.value.trim(),
                ).apply { updateStudyRepository.editStudy(studyId, this.toDomain()) }
            }
                .onSuccess {
                    _studyState.value = State.Success(studyId)
                }.onFailure {
                    _studyState.value = State.FAIL
                }
        }
    }

    fun createStudy() {
        if (isOpenStudy) return
        isOpenStudy = true
        viewModelScope.launch {
            UpdateStudyUiModel(
                name.value.trim(),
                peopleCount.value,
                startDate.value,
                period.value,
                cycle.value,
                introduction.value.trim(),
            ).apply {
                updateStudyRepository.createStudy(this.toDomain())
                    .onSuccess {
                        _studyState.value = State.Success(it)
                    }.onFailure {
                        _studyState.value = State.FAIL
                    }
            }
        }
    }

    sealed interface State {
        data class Success(
            val studyId: Long,
        ) : State

        object FAIL : State
        object IDLE : State
    }

    private fun isInitializeCreateStudyInformation(): Boolean =
        name.value.isNotBlankAndEmpty() && peopleCount.value.isNotZero() && startDate.value.isNotEmpty() && period.value.isNotZero() && cycle.value.date.isNotZero() && introduction.value.isNotBlankAndEmpty()

    private fun UpdateStudyUiModel.toDomain(): UpdateStudy =
        UpdateStudy(name, peopleCount, startDate, period, cycle.toDomain(), introduction)

    private fun PeriodUiModel.toDomain(): Period = Period(date, unit)

    private fun String.isNotBlankAndEmpty(): Boolean = isNotBlank().and(isNotEmpty())

    private fun String.isNotEmpty(): Boolean = isEmpty().not()

    private fun String.isNotBlank(): Boolean = isBlank().not()

    private fun Int.isNotZero(): Boolean = this != 0
}
