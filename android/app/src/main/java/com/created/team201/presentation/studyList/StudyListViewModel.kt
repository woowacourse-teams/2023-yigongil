package com.created.team201.presentation.studyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.team201.presentation.studyList.model.StudySummaryUiModel

class StudyListViewModel : ViewModel() {

    private val _studySummaries = MutableLiveData<List<StudySummaryUiModel>>()
    val studySummaries: LiveData<List<StudySummaryUiModel>>
        get() = _studySummaries

    init {
        _studySummaries.value = listOf()
        loadPage()
    }

    fun loadPage() {
        // if (load Fail)
        //      return
        // if (load Success)
        //      val newItems = _studySummaries.value?.toMutableList()
        //      newItems?.addAll(server data -> pageIndex)
        //      _studySummaries.value = newItems?.toList()
        //      pageIndex++
    }
}
