package com.created.team201.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.UserProfile
import com.created.team201.data.repository.GuestRepository
import com.created.team201.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val guestRepository: GuestRepository,
) : ViewModel() {
    private val _profile: MutableLiveData<UserProfile> = MutableLiveData()
    val profile: LiveData<UserProfile> get() = _profile

    val isGuest: Boolean get() = guestRepository.getIsGuest()

    fun initProfile(userId: Long) {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfile(userId)
            }.onSuccess { userProfile ->
                _profile.value = userProfile
            }
        }
    }
}
