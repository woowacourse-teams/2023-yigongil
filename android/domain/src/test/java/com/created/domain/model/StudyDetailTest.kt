package com.created.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StudyDetailTest {

    @Test
    fun `주어진 날짜가 문자열 "3d"일 때, 숫자 값 3을 출력한다`() {
        // given :
        val date = "3d"

        // when :
        val actual = StudyDetail.getPeriod(date)
        val expected: Int = 3

        // then :
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `주어진 날짜가 문자열 "30w"일 때, 숫자 값 30을 출력한다`() {
        // given :
        val date = "30w"

        // when :
        val actual = StudyDetail.getPeriod(date)
        val expected: Int = 30

        // then :
        assertThat(actual).isEqualTo(expected)
    }
}
