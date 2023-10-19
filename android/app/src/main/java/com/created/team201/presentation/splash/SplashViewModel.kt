package com.created.team201.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.AppUpdateInformation
import com.created.domain.model.response.NetworkResponse
import com.created.domain.repository.AuthRepository
import com.created.domain.repository.OnBoardingRepository
import com.created.domain.repository.SplashRepository
import com.created.team201.presentation.onBoarding.model.OnBoardingDoneState
import com.created.team201.presentation.splash.SplashViewModel.State.FAIL
import com.created.team201.presentation.splash.SplashViewModel.State.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepository: SplashRepository,
    private val authRepository: AuthRepository,
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {

    private val _appUpdateInformation: MutableLiveData<AppUpdateInformation> = MutableLiveData()
    val appUpdateInformation: LiveData<AppUpdateInformation> get() = _appUpdateInformation

    private val _loginState: MutableLiveData<State> = MutableLiveData()
    val loginState: LiveData<State> get() = _loginState

    private val _onBoardingDoneState: MutableLiveData<OnBoardingDoneState> = MutableLiveData()
    val onBoardingDoneState: LiveData<OnBoardingDoneState> get() = _onBoardingDoneState

    fun getAppUpdateInformation(versionCode: Int) {
        splashRepository.getAppUpdateInformation(versionCode) { appUpdateInformation ->
            _appUpdateInformation.postValue(appUpdateInformation)
        }
    }

    fun verifyToken() {
        if (authRepository.accessToken.isEmpty()) {
            _loginState.value = FAIL
            return
        }
        tryLogin()
    }

    private fun tryLogin() {
        viewModelScope.launch {
            when (val response = authRepository.requestSignIn()) {
                is NetworkResponse.Success -> _loginState.value = SUCCESS
                is NetworkResponse.Failure -> {
                    if (response.responseCode == 401) {
                        if (authRepository.renewAccessToken() is NetworkResponse.Success) {
                            _loginState.value = SUCCESS
                        } else {
                            _loginState.value = FAIL
                        }
                    } else {
                        _loginState.value = FAIL
                    }
                }

                else -> _loginState.value = FAIL
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
