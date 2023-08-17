package com.created.domain.model

data class ProfileInformation(
    val nickname: Nickname,
    val introduction: String,
) {
    fun updateMyProfile(nickname: Nickname, introduction: String): ProfileInformation =
        copy(nickname = nickname, introduction = introduction)
}
