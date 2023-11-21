package com.created.team201.presentation.createStudy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.CreateStudy
import com.created.team201.data.repository.CreateStudyRepository
import com.created.team201.presentation.createStudy.model.CreateStudyUiState
import com.created.team201.presentation.createStudy.model.FragmentState
import com.created.team201.presentation.createStudy.model.FragmentState.FirstFragment
import com.created.team201.presentation.createStudy.model.FragmentState.SecondFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateStudyViewModel @Inject constructor(
    private val createStudyRepository: CreateStudyRepository,
) : ViewModel() {
    private val _fragmentState: MutableStateFlow<FragmentState> =
        MutableStateFlow(FirstFragment)
    val fragmentState: StateFlow<FragmentState> get() = _fragmentState.asStateFlow()

    private val _peopleCount: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_INT_VALUE)
    val peopleCount: StateFlow<Int> get() = _peopleCount.asStateFlow()

    private val _studyDate: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_INT_VALUE)
    val studyDate: StateFlow<Int> get() = _studyDate.asStateFlow()

    private val _cycle: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_INT_VALUE)
    val cycle: StateFlow<Int> get() = _cycle.asStateFlow()

    private val _studyName: MutableStateFlow<String> = MutableStateFlow(DEFAULT_STRING_VALUE)
    val studyName: StateFlow<String> get() = _studyName.asStateFlow()

    private val _studyIntroduction: MutableStateFlow<String> =
        MutableStateFlow(DEFAULT_STRING_VALUE)
    val studyIntroduction: StateFlow<String> get() = _studyIntroduction.asStateFlow()

    val isEnableFirstCreateStudyNext: StateFlow<Boolean> =
        combine(peopleCount, studyDate, cycle) { peopleCount, studyDate, cycle ->
            return@combine peopleCount != DEFAULT_INT_VALUE && studyDate != DEFAULT_INT_VALUE && cycle != DEFAULT_INT_VALUE
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val isEnableSecondCreateStudyNext: StateFlow<Boolean> =
        combine(studyName, studyIntroduction) { studyName, studyIntroduction ->
            return@combine studyName.isNotBlankAndEmpty() && studyIntroduction.isNotBlankAndEmpty()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _createStudyUiState: MutableSharedFlow<CreateStudyUiState> = MutableSharedFlow()
    val createStudyUiState: SharedFlow<CreateStudyUiState> get() = _createStudyUiState.asSharedFlow()

    private val _createStudyEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val createStudyEvent: SharedFlow<Event> get() = _createStudyEvent.asSharedFlow()

    private var isOpenStudy: Boolean = false

    fun setPeopleCount(peopleCount: Int) {
        _peopleCount.value = peopleCount
    }

    fun setStudyDate(studyDate: Int) {
        _studyDate.value = studyDate
    }

    fun setCycle(cycle: Int) {
        _cycle.value = cycle
    }

    fun setStudyName(studyName: String) {
        _studyName.value = studyName.replace("\n", "")
    }

    fun setStudyIntroduction(studyIntroduction: String) {
        _studyIntroduction.value = studyIntroduction
    }

    fun navigateToNext() {
        viewModelScope.launch {
            when (_fragmentState.value) {
                is FirstFragment -> {
                    _createStudyEvent.emit(Event.NavigateToNext(fragmentState.value))
                    _fragmentState.value = SecondFragment
                }

                else -> Unit
            }
        }
    }

    fun navigateToBefore() {
        viewModelScope.launch {
            _createStudyEvent.emit(Event.NavigateToBefore(fragmentState.value))
            _fragmentState.value = FirstFragment
        }
    }

    fun createStudy() {
        if (isOpenStudy) return
        isOpenStudy = true
        viewModelScope.launch {
            val study = CreateStudy(
                name = studyName.value.trim(),
                introduction = studyIntroduction.value,
                peopleCount = peopleCount.value,
                studyDate = studyDate.value,
                numberOfStudyPerWeek = cycle.value,
            )
            createStudyRepository.createStudy(study)
                .onSuccess { studyId ->
                    _createStudyEvent.emit(Event.CreateStudySuccess)
                    _createStudyUiState.emit(CreateStudyUiState.Success(studyId))
                }
                .onFailure {
                    _createStudyEvent.emit(Event.CreateStudyFailure)
                    _createStudyUiState.emit(CreateStudyUiState.Fail)
                }
        }
    }

    private fun String.isNotBlankAndEmpty(): Boolean = isNotBlank() and isNotEmpty()

    sealed interface Event {
        object CreateStudySuccess : Event
        object CreateStudyFailure : Event
        data class NavigateToBefore(val fragmentState: FragmentState) : Event
        data class NavigateToNext(val fragmentState: FragmentState) : Event
    }

    companion object {
        private const val DEFAULT_INT_VALUE = -1
        private const val DEFAULT_STRING_VALUE = ""
    }
}
