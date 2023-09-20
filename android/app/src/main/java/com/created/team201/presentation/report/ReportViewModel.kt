package com.created.team201.presentation.report

import android.app.DatePickerDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.ReportStudy
import com.created.domain.model.ReportUser
import com.created.domain.repository.ReportRepository
import com.created.team201.presentation.report.model.DateUiModel
import com.created.team201.presentation.report.model.ReportStudyUiModel
import com.created.team201.presentation.report.model.ReportUserUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
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

    fun reportUser(
        reportedUserId: Long,
        notifySuccessfulReport: () -> Unit,
        notifyUnsuccessfulReport: () -> Unit,
    ) {
        viewModelScope.launch {
            runCatching {
                if (isReporting) {
                    return@launch
                }
                isReporting = true
                reportRepository.reportUser(
                    ReportUserUiModel(
                        reportedMemberId = reportedUserId,
                        title = title.value,
                        problemOccuredAt = selectedDate.value.toProblemOccurredAt(),
                        content = content.value,
                    ).toDomain(),
                )
            }.onSuccess {
                notifySuccessfulReport()
                isReporting = false
            }.onFailure {
                notifyUnsuccessfulReport()
                isReporting = false
            }
        }
    }

    fun reportStudy(
        reportedStudyId: Long,
        notifySuccessfulReport: () -> Unit,
        notifyUnsuccessfulReport: () -> Unit,
    ) {
        viewModelScope.launch {
            runCatching {
                if (isReporting) {
                    return@launch
                }
                isReporting = true
                reportRepository.reportStudy(
                    ReportStudyUiModel(
                        reportedStudyId = reportedStudyId,
                        title = title.value,
                        problemOccurredAt = selectedDate.value.toProblemOccurredAt(),
                        content = content.value,
                    ).toDomain(),
                )
            }.onSuccess {
                notifySuccessfulReport()
                isReporting = false
            }.onFailure {
                notifyUnsuccessfulReport()
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

    private fun ReportUserUiModel.toDomain(): ReportUser = ReportUser(
        reportedMemberId,
        title,
        problemOccuredAt,
        content,
    )

    private fun ReportStudyUiModel.toDomain(): ReportStudy = ReportStudy(
        reportedStudyId,
        title,
        problemOccurredAt,
        content,
    )

    companion object {
        private const val EMPTY_STRING = ""
        private const val NEW_LINE = "/n"
        private const val DATE_FORMAT = "%04d.%02d.%02d"
        private const val MONTH_CALIBRATION_VALUE = 1
    }
}
