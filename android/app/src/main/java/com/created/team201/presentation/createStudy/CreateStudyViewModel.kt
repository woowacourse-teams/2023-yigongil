package com.created.team201.presentation.createStudy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.team201.presentation.createStudy.model.CreateStudyUiModel
import com.created.team201.presentation.createStudy.model.PeriodUiModel

class CreateStudyViewModel : ViewModel() {
    private val name: MutableLiveData<String> = MutableLiveData<String>()
    private val introduction: MutableLiveData<String> = MutableLiveData<String>()

    private val _peopleCount: MutableLiveData<Int> = MutableLiveData<Int>()
    val peopleCount: LiveData<Int>
        get() = _peopleCount

    private val _startDate: MutableLiveData<String> = MutableLiveData<String>()
    val startDate: LiveData<String>
        get() = _startDate

    private val _period: MutableLiveData<Int> = MutableLiveData<Int>()
    val period: LiveData<Int>
        get() = _period

    private val _cycle: MutableLiveData<PeriodUiModel> = MutableLiveData<PeriodUiModel>()
    val cycle: LiveData<PeriodUiModel>
        get() = _cycle

    val isEnableCreateStudy = MediatorLiveData<Boolean>().apply {
        addSourceList(name, introduction, peopleCount, startDate, period, cycle) {
            isInitializeCreateStudyInformation()
        }
    }

    fun setName(name: String) {
        this.name.value = name
    }

    fun setIntroduction(introduction: String) {
        this.introduction.value = introduction
    }

    fun setPeopleCount(peopleCount: Int) {
        _peopleCount.value = peopleCount
    }

    fun setStartDate(startDate: String) {
        _startDate.value = startDate
    }

    fun setCycle(date: Int, type: Int) {
        val cycle = PeriodUiModel(date, type)
        _cycle.value = cycle
    }

    fun setPeriod(period: Int) {
        _period.value = period
    }

    fun getCreateStudy(): CreateStudyUiModel = CreateStudyUiModel(
        name.value!!,
        peopleCount.value!!,
        startDate.value!!,
        period.value!!,
        cycle.value!!,
        introduction.value!!,
    )

    private fun isInitializeCreateStudyInformation(): Boolean =
        name.isInitialized && peopleCount.isInitialized && startDate.isInitialized && period.isInitialized && cycle.isInitialized && introduction.isInitialized

    private fun <T> MediatorLiveData<T>.addSourceList(
        vararg liveDataArgument: LiveData<*>,
        onChanged: () -> T,
    ) {
        liveDataArgument.forEach {
            this.addSource(it) {
                value = onChanged()
            }
        }
    }
}
