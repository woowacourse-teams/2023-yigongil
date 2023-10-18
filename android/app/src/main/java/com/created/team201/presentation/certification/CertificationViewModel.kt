package com.created.team201.presentation.certification

import androidx.lifecycle.ViewModel
import com.created.domain.repository.CertificationRepository
import com.created.team201.presentation.certification.model.CertificationUiState
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
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
}
