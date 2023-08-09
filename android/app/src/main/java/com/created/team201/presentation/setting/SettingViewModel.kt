package com.created.team201.presentation.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.repository.SettingRepository
import com.created.team201.application.Team201App
import com.created.team201.data.datasource.local.TokenDataSourceImpl
import com.created.team201.data.datasource.remote.SettingDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.SettingRepositoryImpl
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository
) : ViewModel() {
    private val _isWithdrawAccountState: MutableLiveData<State> = MutableLiveData()
    val isWithdrawAccountState: LiveData<State>
        get() = _isWithdrawAccountState

    fun logout() {
        settingRepository.logout()
    }

    fun withdrawAccount() {
        viewModelScope.launch {
            settingRepository.requestWithdrawAccount()
                .onSuccess {
                    _isWithdrawAccountState.value = State.SUCCESS
                }.onFailure {
                    _isWithdrawAccountState.value = State.FAIL
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
                SettingViewModel(
                    SettingRepositoryImpl(
                        SettingDataSourceImpl(NetworkServiceModule.settingService),
                        TokenDataSourceImpl(Team201App.provideTokenStorage())
                    )
                )
            }
        }
    }
}
