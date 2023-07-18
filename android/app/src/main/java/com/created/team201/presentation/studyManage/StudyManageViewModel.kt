package com.created.team201.presentation.studyManage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.team201.presentation.studyManage.model.OnGoingStudiesUiModel
import com.created.team201.presentation.studyManage.model.OnGoingStudyStatus.OPENED
import com.created.team201.presentation.studyManage.model.OnGoingStudyStatus.PARTICIPATED
import com.created.team201.presentation.studyManage.model.StudyManageUiModel

class StudyManageViewModel : ViewModel() {

    private val studies: MutableLiveData<List<StudyManageUiModel>> = MutableLiveData()
    private var _onGoingStudies: MutableLiveData<List<OnGoingStudiesUiModel>> = MutableLiveData()
    val onGoingStudiesUiModel: LiveData<List<OnGoingStudiesUiModel>>
        get() = _onGoingStudies

    init {
        studies.value = listOf()
        loadStudies()
    }

    private fun loadStudies() {
        // if (load Fail)
        //      return
        // if (load Success)
        //      studies.value = newItems
        updateStudies()
    }

    private fun updateStudies() {
        _onGoingStudies.value = listOf(
            OnGoingStudiesUiModel(
                status = PARTICIPATED,
                studySummariesUiModel = studies.value?.mapNotNull { item ->
                    item.studySummaryUiModel.takeIf { !item.isMaster }
                } ?: listOf(),
            ),
            OnGoingStudiesUiModel(
                status = OPENED,
                studySummariesUiModel = studies.value?.mapNotNull { item ->
                    item.studySummaryUiModel.takeIf { item.isMaster }
                } ?: listOf(),
            ),
        )
    }
}
