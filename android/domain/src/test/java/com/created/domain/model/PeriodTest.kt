package com.created.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PeriodTest {
    @Test
    fun `날짜가 7일때, 기간의 일 수는 7일이다`() {
        val period = Period(7, 0)

        val actual = period.getPeriodDate(0)
        val expected = 7

        assertEquals(expected, actual)
    }

    @Test
    fun `날짜가 7일때, 기간의 주는 1주이다`() {
        val period = Period(7, 0)

        val actual = period.getPeriodDate(1)
        val expected = 1

        assertEquals(expected, actual)
    }

    @Test
    fun `날짜가 6이면 주는 존재하지 않는다`() {
        val period = Period(6, 0)

        val actual = period.hasPeriodWeekType()
        val expected = false

        assertEquals(expected, actual)
    }
}
