package com.created.team201.presentation.main

import androidx.lifecycle.ViewModel
import com.created.team201.data.repository.GuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val guestRepository: GuestRepository,
) : ViewModel() {
    val isGuest: Boolean get() = guestRepository.getIsGuest()
}
