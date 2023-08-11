package com.created.team201.presentation.onBoarding.model

sealed interface OnBoardingDoneState {
    val isDone: Boolean

    class Success(private val onBoardingState: Boolean) : OnBoardingDoneState {
        override val isDone: Boolean
            get() = onBoardingState
    }

    object FAIL : OnBoardingDoneState {
        override val isDone: Boolean
            get() = false
    }

    object IDLE : OnBoardingDoneState {
        override val isDone: Boolean
            get() = false
    }
}
