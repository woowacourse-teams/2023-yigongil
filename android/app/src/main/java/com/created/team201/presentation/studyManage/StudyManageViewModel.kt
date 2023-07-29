package com.created.team201.presentation.studyManage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.Period
import com.created.domain.StudySummary
import com.created.domain.model.Role.MASTER
import com.created.domain.model.StudyManage
import com.created.domain.repository.StudyManageRepository
import com.created.team201.data.datasource.remote.StudyManageDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.StudyManageRepositoryImpl
import com.created.team201.presentation.studyList.model.PeriodUiModel
import com.created.team201.presentation.studyList.model.StudySummaryUiModel
import com.created.team201.presentation.studyManage.model.MyStudiesUiModel
import com.created.team201.presentation.studyManage.model.MyStudyStatus.OPENED
import com.created.team201.presentation.studyManage.model.MyStudyStatus.PARTICIPATED
import com.created.team201.presentation.studyManage.model.StudyManageUiModel
import kotlinx.coroutines.launch

class StudyManageViewModel(
    private val studyManageRepository: StudyManageRepository,
) : ViewModel() {

    private val studies: MutableLiveData<List<StudyManageUiModel>> = MutableLiveData()
    private var _myStudiesUiModel: MutableLiveData<List<MyStudiesUiModel>> = MutableLiveData()
    val myStudiesUiModel: LiveData<List<MyStudiesUiModel>>
        get() = _myStudiesUiModel

    init {
        studies.value = listOf()
        loadStudies()
    }

    private fun loadStudies() {
        viewModelScope.launch {
            kotlin.runCatching {
                studies.value = studyManageRepository.getMyStudies().toUiModel()
            }.onSuccess {
                updateStudies()
            }.onFailure {
                studies.value = listOf()
            }
        }
    }

    private fun updateStudies() {
        _myStudiesUiModel.value = listOf(
            MyStudiesUiModel(
                status = PARTICIPATED,
                studySummariesUiModel = studies.value?.mapNotNull { item ->
                    item.studySummaryUiModel.takeIf { item.role != MASTER }
                } ?: listOf(),
            ),
            MyStudiesUiModel(
                status = OPENED,
                studySummariesUiModel = studies.value?.mapNotNull { item ->
                    item.studySummaryUiModel.takeIf { item.role == MASTER }
                } ?: listOf(),
            ),
        )
    }

    private fun StudyManage.toUiModel(): StudyManageUiModel =
        StudyManageUiModel(
            role,
            studySummary.toUiModel(),
        )

    private fun StudySummary.toUiModel(): StudySummaryUiModel =
        StudySummaryUiModel(
            id,
            processingStatus,
            tier,
            title,
            date,
            totalRound,
            period.toUiModel(),
            currentMember,
            maximumMember,
        )

    private fun Period.toUiModel(): PeriodUiModel =
        PeriodUiModel(number, unit)

    private fun List<StudyManage>.toUiModel(): List<StudyManageUiModel> =
        this.map { it.toUiModel() }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                StudyManageViewModel(
                    StudyManageRepositoryImpl(
                        StudyManageDataSourceImpl(
                            NetworkServiceModule.studyManageService,
                        ),
                    ),
                )
            }
        }
    }
}
