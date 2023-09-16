package com.created.team201.presentation.guest

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
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.AuthRepositoryImpl
import com.created.team201.data.repository.OnBoardingRepositoryImpl
import com.created.team201.presentation.guest.GuestViewModel.State.FAIL
import com.created.team201.presentation.guest.GuestViewModel.State.SUCCESS
import com.created.team201.presentation.onBoarding.model.OnBoardingDoneState
import kotlinx.coroutines.launch

class GuestViewModel(
    private val authRepository: AuthRepository,
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {
    private val _signUpState: MutableLiveData<State> = MutableLiveData()
    val signUpState: LiveData<State> get() = _signUpState

    private val _onBoardingDoneState: MutableLiveData<OnBoardingDoneState> = MutableLiveData()
    val onBoardingDoneState: LiveData<OnBoardingDoneState> get() = _onBoardingDoneState

    private val _refreshState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val refreshState: LiveData<Boolean> get() = _refreshState

    fun refresh() {
        _refreshState.value = true
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                GuestViewModel(
                    AuthRepositoryImpl(
                        NetworkServiceModule.authService,
                        TokenDataSourceImpl(
                            Team201App.provideTokenStorage(),
                        ),
                    ),
                    OnBoardingRepositoryImpl(
                        OnBoardingIsDoneDataSourceImpl(
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
