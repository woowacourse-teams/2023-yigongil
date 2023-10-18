package com.created.team201.presentation.certification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Certification
import com.created.domain.repository.CertificationRepository
import com.created.team201.presentation.certification.model.CertificationUiState
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository,
) : ViewModel() {

    private val _uiState: NonNullMutableLiveData<CertificationUiState> =
        NonNullMutableLiveData(CertificationUiState.Editing())
    val uiState: NonNullLiveData<CertificationUiState>
        get() = _uiState

    fun updateImage(imageUrl: String) {
        _uiState.value = CertificationUiState.getState(imageUrl, uiState.value.content)
    }

    fun updateContent(content: String) {
        _uiState.value = CertificationUiState.getState(uiState.value.imageUrl, content)
    }

    fun updateCertification(file: File, studyId: Long) {
        if (studyId == CertificationActivity.KEY_NOT_FOUND_STUDY_ID) {
            _uiState.value = CertificationUiState.Failure(content = uiState.value.content)
            return
        }
        viewModelScope.launch {
            postImage(file)
            postCertification(studyId)
        }
    }

    private fun postImage(file: File) {
        viewModelScope.launch {
            certificationRepository.postImage(file)
                .onSuccess { url ->
                    if (url.isBlank()) {
                        _uiState.value =
                            CertificationUiState.Failure(content = uiState.value.content)
                    } else {
                        _uiState.value = CertificationUiState.getState(url, uiState.value.content)
                    }
                }.onFailure {
                    _uiState.value = CertificationUiState.Failure(content = uiState.value.content)
                }
        }
    }

    private fun postCertification(studyId: Long) {
        viewModelScope.launch {
            runCatching {
                certificationRepository.postCertification(
                    studyId,
                    Certification(uiState.value.imageUrl, uiState.value.content),
                )
            }.onSuccess {
                _uiState.value =
                    CertificationUiState.Success(uiState.value.imageUrl, uiState.value.content)
            }.onFailure {
                _uiState.value =
                    CertificationUiState.Failure(uiState.value.imageUrl, uiState.value.content)
            }
        }
    }
}
