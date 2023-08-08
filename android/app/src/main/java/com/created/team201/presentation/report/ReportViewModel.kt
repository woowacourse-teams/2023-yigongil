package com.created.team201.presentation.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    fun setTitle(title: String) {
        _title.value = title.trim().replace(NEW_LINE, EMPTY_STRING)
    }

    fun setContent(content: String) {
        _content.value = content.trim()
    }

    private fun isInputsNotEmptyAndNotBlank(): Boolean {
        val title = title.value ?: EMPTY_STRING
        val content = content.value ?: EMPTY_STRING
        return title.isNotEmptyAndNotBlack() && content.isNotEmptyAndNotBlack()
    }

    private fun String.isNotEmptyAndNotBlack(): Boolean = isNotEmpty() && isNotBlank()

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

    companion object {
        private const val EMPTY_STRING = ""
        private const val NEW_LINE = "/n"
    }
}
