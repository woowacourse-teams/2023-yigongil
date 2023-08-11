package com.created.team201.presentation.report

import android.app.DatePickerDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Report
import com.created.domain.repository.ReportRepository
import com.created.team201.data.datasource.remote.ReportDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.ReportRepositoryImpl
import com.created.team201.presentation.report.model.DateUiModel
import com.created.team201.presentation.report.model.ReportTargetUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import kotlinx.coroutines.launch
import java.util.Calendar

class ReportViewModel(
    private val reportRepository: ReportRepository,
) : ViewModel() {

    private val _title: NonNullMutableLiveData<String> = NonNullMutableLiveData(EMPTY_STRING)
    val title: NonNullLiveData<String> get() = _title

    private val _content: NonNullMutableLiveData<String> = NonNullMutableLiveData(EMPTY_STRING)
    val content: NonNullLiveData<String> get() = _content

    private var isReporting: Boolean = false

    private val _isEnableReport: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSourceList(title, content) {
            isInputsNotEmptyAndNotBlank()
        }
    }
    val isEnableReport: LiveData<Boolean> get() = _isEnableReport

    private val _selectedDate: NonNullMutableLiveData<DateUiModel> =
        NonNullMutableLiveData(initDate())
    val selectedDate: NonNullLiveData<DateUiModel> get() = _selectedDate

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
        val title = title.value
        val content = content.value
        return title.isNotBlank() && content.isNotBlank()
    }

    fun reportUser(targetId: Long, notifySuccessfulReport: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                if (isReporting) {
                    return@launch
                }
                isReporting = true
                reportRepository.reportUser(
                    ReportTargetUiModel(
                        reportedMemberId = targetId,
                        title = title.value,
                        problemOccuredAt = selectedDate.value.toProblemOccurredAt(),
                        content = content.value,
                    ).toDomain(),
                )
            }.onSuccess {
                notifySuccessfulReport()
                isReporting = false
            }.onFailure {
                isReporting = false
            }
        }
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

    private fun DateUiModel.toProblemOccurredAt(): String = DATE_FORMAT.format(year, month, day)

    private fun ReportTargetUiModel.toDomain(): Report = Report(
        reportedMemberId,
        title,
        problemOccuredAt,
        content,
    )

    companion object {
        private const val EMPTY_STRING = ""
        private const val NEW_LINE = "/n"
        private const val DATE_FORMAT = "%04d.%02d.%02d"

        private const val MONTH_CALIBRATION_VALUE = 1
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = ReportRepositoryImpl(
                    ReportDataSourceImpl(
                        NetworkServiceModule.reportService,
                    ),
                )
                return ReportViewModel(repository) as T
            }
        }
    }
}
