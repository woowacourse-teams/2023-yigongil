package com.created.team201.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.repository.AuthRepository
import com.created.domain.repository.GuestRepository
import com.created.domain.repository.OnBoardingRepository
import com.created.team201.application.Team201App
import com.created.team201.data.datasource.local.DefaultOnBoardingDataSource
import com.created.team201.data.datasource.local.DefaultTokenDataSource
import com.created.team201.data.datasource.remote.OnBoardingDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.DefaultAuthRepository
import com.created.team201.data.repository.DefaultGuestRepository
import com.created.team201.data.repository.DefaultOnBoardingRepository
import com.created.team201.presentation.login.LoginViewModel.State.FAIL
import com.created.team201.presentation.login.LoginViewModel.State.SUCCESS
import com.created.team201.presentation.onBoarding.model.OnBoardingDoneState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val guestRepository: GuestRepository,
    private val authRepository: AuthRepository,
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {
    private val _signUpState: MutableLiveData<State> = MutableLiveData()
    val signUpState: LiveData<State> get() = _signUpState

    private val _signUpGuestState: MutableLiveData<State> = MutableLiveData()
    val signUpGuestState: LiveData<State> get() = _signUpGuestState

    private val _onBoardingDoneState: MutableLiveData<OnBoardingDoneState> = MutableLiveData()
    val onBoardingDoneState: LiveData<OnBoardingDoneState> get() = _onBoardingDoneState

    fun signUp(token: String) {
        viewModelScope.launch {
            authRepository.requestSignUp(token)
                .onSuccess {
                    _signUpState.value = SUCCESS
                }.onFailure {
                    _signUpState.value = FAIL
                    Log.e("FAIL_ERROR", "SIGN_UP : ${it.message}")
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

    fun signUpGuest() {
        guestRepository.signUpGuest()
        _signUpGuestState.value = SUCCESS
    }

    sealed interface State {
        object SUCCESS : State
        object FAIL : State
        object IDLE : State
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LoginViewModel(
                    DefaultGuestRepository(
                        DefaultTokenDataSource(Team201App.provideTokenStorage()),
                    ),
                    DefaultAuthRepository(
                        NetworkServiceModule.authService,
                        DefaultTokenDataSource(Team201App.provideTokenStorage()),
                    ),
                    DefaultOnBoardingRepository(
                        DefaultOnBoardingDataSource(
                            Team201App.provideOnBoardingIsDoneStorage(),
                        ),
                        OnBoardingDataSourceImpl(
                            NetworkServiceModule.onBoardingService,
                        ),
                    ),
                )
            }
        }
    }
}
