package com.created.team201.presentation.splash.model

enum class AppUpdateType {
    IMMEDIATE,
    FLEXIBLE,
    ;

    companion object {
        fun from(shouldUpdate: Boolean): AppUpdateType =
            when (shouldUpdate) {
                true -> IMMEDIATE
                false -> FLEXIBLE
            }
    }
}
