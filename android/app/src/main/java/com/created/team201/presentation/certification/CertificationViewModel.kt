package com.created.team201.presentation.certification

import androidx.lifecycle.ViewModel
import com.created.domain.repository.CertificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository,
) : ViewModel()
