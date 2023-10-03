package com.created.team201.presentation.createStudy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.team201.presentation.createStudy.model.CreateStudyUiModel
import com.created.team201.presentation.createStudy.model.FragmentState
import com.created.team201.presentation.createStudy.model.FragmentState.FirstFragment
import com.created.team201.presentation.createStudy.model.FragmentState.SecondFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateStudyViewModel @Inject constructor(

) : ViewModel() {
    private val _fragmentState: MutableStateFlow<FragmentState> =
        MutableStateFlow(FirstFragment)
    val fragmentState: StateFlow<FragmentState> get() = _fragmentState.asStateFlow()

    private val _peopleCount: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_INT_VALUE)
    val peopleCount: StateFlow<Int> get() = _peopleCount.asStateFlow()

    private val _studyDate: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_INT_VALUE)
    val studyDate: StateFlow<Int> get() = _studyDate.asStateFlow()

    private val _studyName: MutableStateFlow<String> = MutableStateFlow(DEFAULT_STRING_VALUE)
    val studyName: StateFlow<String> get() = _studyName.asStateFlow()

    private val _studyIntroduction: MutableStateFlow<String> =
        MutableStateFlow(DEFAULT_STRING_VALUE)
    val studyIntroduction: StateFlow<String> get() = _studyIntroduction.asStateFlow()

    val isEnableFirstCreateStudyNext: Flow<Boolean> =
        combine(peopleCount, studyDate) { peopleCount, studyDate ->
            return@combine peopleCount != DEFAULT_INT_VALUE && studyDate != DEFAULT_INT_VALUE
        }

    val isEnableSecondCreateStudyNext: Flow<Boolean> =
        combine(studyName, studyIntroduction) { studyName, studyIntroduction ->
            return@combine studyName.isNotBlankAndEmpty() && studyIntroduction.isNotBlankAndEmpty()
        }

    private var isOpenStudy: Boolean = false

    fun setPeopleCount(peopleCount: Int) {
        _peopleCount.value = peopleCount
    }

    fun setStudyDate(studyDate: Int) {
        _studyDate.value = studyDate
    }

    fun setStudyName(studyName: String) {
        _studyName.value = studyName.replace("\n", "")
    }

    fun setStudyIntroduction(studyIntroduction: String) {
        _studyIntroduction.value = studyIntroduction
    }

    fun navigateToNext() {
        when (_fragmentState.value) {
            is FirstFragment -> {
                _fragmentState.value = SecondFragment
            }

            else -> Unit
        }
    }

    fun navigateToBefore() {
        when (_fragmentState.value) {
            is SecondFragment -> {
                _fragmentState.value = FirstFragment
            }

            else -> Unit
        }
    }

    fun createStudy() {
        if (isOpenStudy) return
        isOpenStudy = true
        viewModelScope.launch {
            CreateStudyUiModel(
                name = studyName.value.trim(),
                introduction = studyIntroduction.value,
                peopleCount = peopleCount.value,
                studyDate = studyDate.value,
            ).apply {
                // TODO 스터디 생성 통신
            }
        }
    }

    private fun String.isNotBlankAndEmpty(): Boolean = isNotBlank().and(isNotEmpty())

    companion object {
        private const val DEFAULT_INT_VALUE = -1
        private const val DEFAULT_STRING_VALUE = ""
    }
}
