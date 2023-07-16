package com.created.team201.presentation.studyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class StudyListViewModel : ViewModel() {

    private var pageIndex: Int = 0
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
        //      newItems?.addAll(server data)
        //      _studySummaries.value = newItems?.toList()
        //      pageIndex++
    }
}
