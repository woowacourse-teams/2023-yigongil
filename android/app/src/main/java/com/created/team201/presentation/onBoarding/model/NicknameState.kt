package com.created.team201.presentation.onBoarding.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.created.team201.R

sealed interface NicknameState {
    @get:ColorRes
    val color: Int

    @get:StringRes
    val introduction: Int

    object AVAILABLE : NicknameState {
        override val color: Int
            get() = R.color.blue_9BB9F2
        override val introduction: Int
            get() = R.string.onBoarding_available_nickname
    }

    object UNAVAILABLE : NicknameState {
        override val color: Int
            get() = R.color.red_EB7A7A
        override val introduction: Int
            get() = R.string.onBoarding_unAvailable_nickname
    }

    object DUPLICATE : NicknameState {
        override val color: Int
            get() = R.color.red_EB7A7A
        override val introduction: Int
            get() = R.string.onBoarding_duplicate_nickname
    }
}
