package com.created.domain.model

data class AppUpdateInformation(
    val shouldUpdate: Boolean = false,
    val message: String = "",
)
