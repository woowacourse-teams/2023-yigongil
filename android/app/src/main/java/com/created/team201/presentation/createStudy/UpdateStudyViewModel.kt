package com.created.team201.presentation.createStudy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.model.CreateStudy
import com.created.domain.model.Period
import com.created.domain.model.PeriodUnit
import com.created.domain.repository.CreateStudyRepository
import com.created.team201.data.datasource.remote.CreateStudyDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.CreateStudyRepositoryImpl
import com.created.team201.presentation.createStudy.model.CreateStudyUiModel
import com.created.team201.presentation.createStudy.model.PeriodUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import com.created.team201.util.addSourceList
import kotlinx.coroutines.launch

class UpdateStudyViewModel(
    private val createStudyRepository: CreateStudyRepository,
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

    fun createStudy() {
        if (isOpenStudy) return
        isOpenStudy = true
        viewModelScope.launch {
            CreateStudyUiModel(
                name.value.trim(),
                peopleCount.value,
                startDate.value,
                period.value,
                cycle.value,
                introduction.value.trim(),
            ).apply {
                createStudyRepository.createStudy(this.toDomain())
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

    private fun CreateStudyUiModel.toDomain(): CreateStudy =
        CreateStudy(name, peopleCount, startDate, period, cycle.toDomain(), introduction)

    private fun PeriodUiModel.toDomain(): Period = Period(date, unit)

    private fun String.isNotBlankAndEmpty(): Boolean = isNotBlank().and(isNotEmpty())

    private fun String.isNotEmpty(): Boolean = isEmpty().not()

    private fun String.isNotBlank(): Boolean = isBlank().not()

    private fun Int.isNotZero(): Boolean = this != 0

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = CreateStudyRepositoryImpl(
                    CreateStudyDataSourceImpl(
                        NetworkServiceModule.createStudyService,
                    ),
                )
                UpdateStudyViewModel(repository)
            }
        }
    }
}
