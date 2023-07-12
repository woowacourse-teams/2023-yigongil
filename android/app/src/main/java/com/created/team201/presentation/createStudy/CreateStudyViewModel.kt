package com.created.team201.presentation.createStudy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateStudyViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val introduction = MutableLiveData<String>()
    val peopleCount = MutableLiveData<Int>()
}
