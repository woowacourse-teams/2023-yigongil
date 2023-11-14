package com.created.team201.presentation.guest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.team201.data.repository.AuthRepository
import com.created.team201.data.repository.OnBoardingRepository
import com.created.team201.presentation.guest.GuestViewModel.State.FAIL
import com.created.team201.presentation.guest.GuestViewModel.State.SUCCESS
import com.created.team201.presentation.onBoarding.model.OnBoardingDoneState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuestViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {
    private val _signUpState: MutableLiveData<State> = MutableLiveData()
    val signUpState: LiveData<State> get() = _signUpState

    private val _onBoardingDoneState: MutableLiveData<OnBoardingDoneState> = MutableLiveData()
    val onBoardingDoneState: LiveData<OnBoardingDoneState> get() = _onBoardingDoneState

    private val _refreshState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val refreshState: LiveData<Boolean> get() = _refreshState

    fun updateRefreshState(isRefreshed: Boolean) {
        _refreshState.value = isRefreshed
    }

    fun signUp(token: String) {
        viewModelScope.launch {
            authRepository.requestSignUp(token)
                .onSuccess {
                    _signUpState.value = SUCCESS
                }.onFailure {
                    _signUpState.value = FAIL
                }
        }
    }

    fun getIsOnboardingDone() {
        viewModelScope.launch {
            onBoardingRepository.getIsOnboardingDone()
                .onSuccess { state ->
                    _onBoardingDoneState.value = OnBoardingDoneState.Success(state)
                }.onFailure {
                    _onBoardingDoneState.value = OnBoardingDoneState.FAIL
                }
        }
    }

    sealed interface State {
        object SUCCESS : State
        object FAIL : State
        object IDLE : State
    }
}
