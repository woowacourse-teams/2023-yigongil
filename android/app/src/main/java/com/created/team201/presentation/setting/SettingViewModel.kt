package com.created.team201.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.repository.SettingRepository
import com.created.team201.application.Team201App
import com.created.team201.data.datasource.local.TokenDataSourceImpl
import com.created.team201.data.datasource.remote.SettingDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.SettingRepositoryImpl

class SettingViewModel(
    private val settingRepository: SettingRepository
) : ViewModel() {

    fun logout() {
        settingRepository.logout()
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
