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
import com.created.team201.data.datasource.local.TokenDataSourceImpl
import com.created.team201.data.datasource.remote.login.AuthDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.AuthRepositoryImpl
import com.created.team201.presentation.login.LoginViewModel.State.FAIL
import com.created.team201.presentation.login.LoginViewModel.State.SUCCESS
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _loginState: MutableLiveData<State> = MutableLiveData()
    val loginState: LiveData<State> get() = _loginState

    fun signUp(token: String) {
        viewModelScope.launch {
            authRepository.signUp(token)
                .onSuccess {
                    _loginState.value = SUCCESS
                }.onFailure {
                    _loginState.value = FAIL
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
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LoginViewModel(
                    AuthRepositoryImpl(
                        AuthDataSourceImpl(
                            NetworkServiceModule.authService,
                        ),
                        TokenDataSourceImpl(),
                    ),
                )
            }
        }
    }
}
