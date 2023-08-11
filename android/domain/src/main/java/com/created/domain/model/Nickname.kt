package com.created.domain.model

import java.util.regex.Pattern

data class Nickname(
    val nickname: String,
) {
    init {
        require(validateNickname(nickname)) { ERROR_UNAVAILABLE_NICKNAME }
    }

    fun updateNickname(nickname: String): Nickname = copy(nickname = nickname)

    private fun validateNickname(nickname: String): Boolean =
        isAvailableCharacter(nickname) && isAvailableLength(nickname)

    private fun isAvailableCharacter(nickname: String): Boolean =
        nickname.isBlank() || PATTERN_NICKNAME.matcher(nickname).matches()

    private fun isAvailableLength(nickname: String): Boolean =
        nickname.length in MIN_NICKNAME_LENGTH.rangeTo(MAX_NICKNAME_LENGTH)

    companion object {
        private val PATTERN_NICKNAME = Pattern.compile("^[_a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]+$")
        private const val MIN_NICKNAME_LENGTH = 2
        private const val MAX_NICKNAME_LENGTH = 8
        private const val ERROR_UNAVAILABLE_NICKNAME = "사용 불가한 넥네임입니다"
    }
}
