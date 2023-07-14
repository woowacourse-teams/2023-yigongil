package com.created.team201.presentation.createStudy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.team201.presentation.createStudy.model.PeriodUiModel

class CreateStudyViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val introduction = MutableLiveData<String>()
    val peopleCount = MutableLiveData<Int>()
    val startDate = MutableLiveData<String>()
    val period = MutableLiveData<PeriodUiModel>()
}
