package com.created.team201.presentation.myPage

import com.created.domain.model.Nickname
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation

object MyPageFixture {
    val profile: Profile = Profile(
        githubId = "kimjinwoo",
        id = 1,
        profileInformation = ProfileInformation(
            nickname = Nickname("최강전사김진우"),
            introduction = "안녕하세요, 김진우입니다.",
        ),
        profileImageUrl = null,
        successRate = 77.4,
        successfulRoundCount = 24,
        tier = 1,
        tierProgress = 50,
    )
}
