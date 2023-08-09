package com.created.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class NicknameTest {
    @Test
    fun `닉네임은 2~8자 한글, 영문, 숫자, 언더바의 조합이어야 한다`() {
        val nickname = "나니_12345"
        assertDoesNotThrow { Nickname(nickname) }
    }

    @Test
    fun `닉네임을 변경할 수 있다`() {
        val oldNickname = Nickname("나니_12")
        assertDoesNotThrow { oldNickname.updateNickname("가가_12") }
    }
}
