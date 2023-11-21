package com.created.team201.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.team201.data.repository.GuestRepository
import com.created.team201.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val guestRepository: GuestRepository,
) : ViewModel() {

    private var _uiState: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState>
        get() = _uiState.asStateFlow()

    val isGuest: Boolean get() = guestRepository.getIsGuest()

    fun loadProfile(userId: Long) {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfile(userId)
            }.onSuccess {
                _uiState.value = ProfileUiState.Success(it)
            }.onFailure {
                _uiState.value = ProfileUiState.Failure
            }
        }
    }
}
