package com.created.team201.presentation.onBoarding

import com.created.domain.model.Nickname
import com.created.domain.model.OnBoarding

object OnBoardingFixture {
    val nickname: Nickname = Nickname("김김진진우우")
    const val introduction: String = "안녕"
    val onBoarding: OnBoarding = OnBoarding(nickname, introduction)
}
