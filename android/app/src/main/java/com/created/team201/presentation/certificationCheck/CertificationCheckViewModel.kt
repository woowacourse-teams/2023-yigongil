package com.created.team201.presentation.certificationCheck

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.response.NetworkResponse
import com.created.domain.repository.CertificationRepository
import com.created.team201.presentation.certificationCheck.model.MemberCertificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CertificationCheckViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository,
) : ViewModel() {

    private var _uiState: MutableLiveData<MemberCertificationUiState> = MutableLiveData(MemberCertificationUiState.Loading)
    val uiState: LiveData<MemberCertificationUiState>
        get() = _uiState

    fun getMemberCertification(studyId: Long, certificationId: Long) {
        viewModelScope.launch {
            val response = certificationRepository.getMemberCertification(studyId, certificationId)
            _uiState.value = when (response) {
                is NetworkResponse.Success -> MemberCertificationUiState.Success.getState(response.body)
                is NetworkResponse.Failure -> MemberCertificationUiState.Failure
                is NetworkResponse.NetworkError -> MemberCertificationUiState.Loading
                is NetworkResponse.Unexpected -> MemberCertificationUiState.Loading
            }
        }
    }
}
