package com.created.team201.presentation.certification.model

sealed interface CertificationUiState {
    val imageUrl: String
    val content: String

    data class Editing(
        override val imageUrl: String = "",
        override val content: String = "",
    ) : CertificationUiState

    data class Complete(
        override val imageUrl: String,
        override val content: String,
    ) : CertificationUiState

    data class Failure(
        override val imageUrl: String,
        override val content: String,
    ) : CertificationUiState

    companion object {
        fun getState(imageUrl: String, content: String): CertificationUiState {
            return when (imageUrl.isNotBlank() and content.isNotBlank()) {
                true -> Complete(imageUrl, content)
                false -> Editing(imageUrl, content)
            }
        }
    }
}
