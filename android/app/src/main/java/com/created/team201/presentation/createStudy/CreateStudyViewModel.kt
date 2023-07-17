package com.created.team201.presentation.createStudy

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.domain.model.Period
import com.created.team201.presentation.createStudy.model.CreateStudyUiModel
import com.created.team201.presentation.createStudy.model.PeriodUiModel

class CreateStudyViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val introduction = MutableLiveData<String>()
    val peopleCount = MutableLiveData<Int>()
    val startDate = MutableLiveData<String>()
    val period = MutableLiveData<PeriodUiModel>()
    val cycle = MutableLiveData<PeriodUiModel>()

    val cycleMaxDates = MutableLiveData<Array<Int>>()

    val isEnableCreateStudy = MediatorLiveData<Boolean>().apply {
        addSourceList(name, introduction, peopleCount, startDate, period, cycle) {
            isInitializeCreateStudyInformation()
        }
    }

    fun getCycleDateMaxValue(type: Int): Int {
        period.value?.let {
            return it.toModel().getPeriodDate(type)
        }
        return 0
    }

    fun getCycleTypeMaxValue(): Int {
        period.value?.let {
            if (it.toModel().hasPeriodWeekType().not()) {
                return 0
            }
        }
        return 1
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

    private fun PeriodUiModel.toModel(): Period = Period(date, type)
    private fun <T> MediatorLiveData<T>.addSourceList(
        vararg liveDataArgument: MutableLiveData<*>,
        onChanged: () -> T,
    ) {
        liveDataArgument.forEach {
            this.addSource(it) {
                value = onChanged()
            }
        }
    }
}
