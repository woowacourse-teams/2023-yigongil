package com.created.team201.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.UserStudy
import com.created.domain.repository.HomeRepository
import com.created.team201.presentation.home.HomeViewModel.UserStudyState.Idle
import com.created.team201.presentation.home.HomeViewModel.UserStudyState.Joined
import com.created.team201.presentation.home.HomeViewModel.UserStudyState.Nothing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _userStudyUiState: MutableStateFlow<UserStudyState> = MutableStateFlow(Idle)
    val userStudyUiState: StateFlow<UserStudyState> get() = _userStudyUiState

    init {
        updateUserStudy()
    }

    private fun updateUserStudy() {
        viewModelScope.launch {
            runCatching {
                homeRepository.getUserStudies()
            }.onSuccess { userStudies ->
                when (userStudies.isEmpty()) {
                    true -> _userStudyUiState.value = Nothing
                    false -> _userStudyUiState.value = Joined(userStudies)
                }
            }.onFailure { Log.d("error-HomeViewModel", it.message.toString()) }
        }
    }

    sealed interface UserStudyState {
        data class Joined(
            val userStudies: List<UserStudy>
        ) : UserStudyState

        object Nothing : UserStudyState
        object Idle : UserStudyState
    }
}
