package com.created.team201.presentation.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.team201.data.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
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
}
