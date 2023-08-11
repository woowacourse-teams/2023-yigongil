package com.created.team201.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.repository.AuthRepository
import com.created.domain.repository.OnBoardingRepository
import com.created.team201.application.Team201App
import com.created.team201.data.datasource.local.OnBoardingIsDoneDataSourceImpl
import com.created.team201.data.datasource.local.TokenDataSourceImpl
import com.created.team201.data.datasource.remote.OnBoardingDataSourceImpl
import com.created.team201.data.datasource.remote.login.AuthDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.AuthRepositoryImpl
import com.created.team201.data.repository.OnBoardingRepositoryImpl
import com.created.team201.presentation.onBoarding.model.OnBoardingDoneState
import com.created.team201.presentation.splash.SplashViewModel.State.FAIL
import com.created.team201.presentation.splash.SplashViewModel.State.SUCCESS
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository,
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {
    private val _loginState: MutableLiveData<State> = MutableLiveData()
    val loginState: LiveData<State> get() = _loginState

    private val _onBoardingDoneState: MutableLiveData<OnBoardingDoneState> = MutableLiveData()
    val onBoardingDoneState: LiveData<OnBoardingDoneState> get() = _onBoardingDoneState

    init {
        tryLogin()
    }

    private fun tryLogin() {
        viewModelScope.launch {
            authRepository.requestSignIn()
                .onSuccess {
                    _loginState.value = SUCCESS
                }
                .onFailure {
                    _loginState.value = FAIL
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SplashViewModel(
                    AuthRepositoryImpl(
                        AuthDataSourceImpl(
                            NetworkServiceModule.authService,
                        ),
                        TokenDataSourceImpl(Team201App.provideTokenStorage()),
                    ),
                    OnBoardingRepositoryImpl(
                        OnBoardingIsDoneDataSourceImpl(
                            Team201App.provideOnBoardingIsDoneStorage()
                        ),
                        OnBoardingDataSourceImpl(
                            NetworkServiceModule.onBoardingService,
                        ),
                    )
                )
            }
        }
    }
}
