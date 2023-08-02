package com.created.team201.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.test.core.app.ApplicationProvider
import com.created.domain.repository.AuthRepository
import com.created.team201.data.datasource.local.TokenDataSourceImpl
import com.created.team201.data.datasource.local.TokenStorage
import com.created.team201.data.datasource.remote.login.AuthDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.AuthRepositoryImpl
import com.created.team201.presentation.login.LoginViewModel.State.FAIL
import com.created.team201.presentation.login.LoginViewModel.State.SUCCESS
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _signUpState: MutableLiveData<State> = MutableLiveData()
    val signUpState: LiveData<State> get() = _signUpState

    fun signUp(token: String) {
        viewModelScope.launch {
            authRepository.signUp(token)
                .onSuccess {
                    _signUpState.value = SUCCESS
                }.onFailure {
                    _signUpState.value = FAIL
                    Log.e("FAIL_ERROR", "SIGN_UP : ${it.message}")
                }
        }
    }

    sealed interface State {
        object SUCCESS : State
        object FAIL : State
        object IDLE : State
    }

    companion object {

        fun createTokenStorage(
            tokenStorage: TokenStorage,
        ): TokenStorage = tokenStorage

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LoginViewModel(
                    AuthRepositoryImpl(
                        AuthDataSourceImpl(
                            NetworkServiceModule.authService,
                        ),
                        TokenDataSourceImpl(
                            TokenStorage.getInstance(context = ApplicationProvider.getApplicationContext()),
                        ),
                    ),
                )
            }
        }
    }
}
