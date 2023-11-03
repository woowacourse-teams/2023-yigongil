package com.created.team201.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.team201.data.model.UserStudyEntity
import com.created.team201.data.repository.UserStudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userStudyRepository: UserStudyRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = flow { emit(userStudyRepository.getUserStudies()) }
        .map { HomeUiState.Success(it) }
        .catch { HomeUiState.Failed }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = HomeUiState.Loading
        )
}

sealed interface HomeUiState {
    data class Success(val userStudies: List<UserStudyEntity>) : HomeUiState
    object Loading : HomeUiState
    object Failed : HomeUiState
}