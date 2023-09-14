package com.created.team201.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.repository.GuestRepository
import com.created.team201.application.Team201App
import com.created.team201.data.datasource.local.TokenDataSourceImpl
import com.created.team201.data.repository.GuestRepositoryImpl

class MainViewModel(
    private val guestRepository: GuestRepository,
) : ViewModel() {
    val isGuest: Boolean get() = guestRepository.getIsGuest()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(
                    GuestRepositoryImpl(
                        TokenDataSourceImpl(Team201App.provideTokenStorage())
                    )
                )
            }
        }
    }
}
