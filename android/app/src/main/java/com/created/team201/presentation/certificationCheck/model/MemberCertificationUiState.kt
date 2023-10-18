package com.created.team201.presentation.certificationCheck.model

import com.created.domain.model.MemberCertification

sealed interface MemberCertificationUiState {
    data class Success(
        val imageUrl: String,
        val content: String,
    ) : MemberCertificationUiState {

        companion object {
            fun getState(certification: MemberCertification): MemberCertificationUiState {
                return Success(certification.imageUrl, certification.content)
            }
        }
    }

    object Loading : MemberCertificationUiState

    object Failure : MemberCertificationUiState
}
