package com.created.team201.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.UserStudy
import com.created.domain.repository.HomeRepository
import com.created.team201.presentation.home.HomeViewModel.HomeUiState.FAIL
import com.created.team201.presentation.home.HomeViewModel.HomeUiState.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(SUCCESS(emptyList()))
    val uiState: StateFlow<HomeUiState> get() = _uiState

    init {
        updateUserStudies()
    }

    private fun updateUserStudies() {
        viewModelScope.launch {
            runCatching {
                homeRepository.getUserStudies()
            }.onSuccess { userStudies ->
                _uiState.update { SUCCESS(userStudies) }
            }.onFailure { error -> _uiState.update { FAIL(error) } }
        }
    }

    sealed interface HomeUiState {
        data class SUCCESS(
            val homeStudies: List<UserStudy>
        ) : HomeUiState

        data class FAIL(val error: Throwable) : HomeUiState
        object IDLE : HomeUiState
    }
}
