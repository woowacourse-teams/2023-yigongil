package com.created.team201.presentation.report

import android.app.DatePickerDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.team201.presentation.report.model.DateUiModel
import java.util.Calendar

class ReportViewModel : ViewModel() {

    private val _title: MutableLiveData<String> = MutableLiveData(EMPTY_STRING)
    val title: LiveData<String> get() = _title

    private val _content: MutableLiveData<String> = MutableLiveData(EMPTY_STRING)
    val content: LiveData<String> get() = _content

    private val _isEnableReport: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSourceList(title, content) {
            isInputsNotEmptyAndNotBlank()
        }
    }
    val isEnableReport: LiveData<Boolean> get() = _isEnableReport

    private val _selectedDate: MutableLiveData<DateUiModel> = MutableLiveData(initDate())
    val selectedDate: LiveData<DateUiModel> get() = _selectedDate

    private fun initDate(): DateUiModel {
        val cal = Calendar.getInstance()
        return DateUiModel(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + MONTH_CALIBRATION_VALUE,
            cal.get(Calendar.DAY_OF_MONTH),
        )
    }

    fun setTitle(title: String) {
        _title.value = title.trim().replace(NEW_LINE, EMPTY_STRING)
    }

    fun setContent(content: String) {
        _content.value = content.trim()
    }

    private fun isInputsNotEmptyAndNotBlank(): Boolean {
        val title = title.value ?: EMPTY_STRING
        val content = content.value ?: EMPTY_STRING
        return title.isNotBlank() && content.isNotBlank()
    }

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

    fun updateDate() = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        _selectedDate.value = DateUiModel(year, month + MONTH_CALIBRATION_VALUE, day)
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val NEW_LINE = "/n"
        private const val MONTH_CALIBRATION_VALUE = 1
    }
}
