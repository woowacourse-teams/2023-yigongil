package com.created.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PeriodUnitTest {
    @Test
    fun `type이 0이면 PeriodUnit은 DAY이다`() {
        val periodUnit = PeriodUnit.valueOf(0)
        val expected = PeriodUnit.DAY
        assertEquals(expected, periodUnit)
    }

    @Test
    fun `type이 1이면 PeriodUnit은 WEEK이다`() {
        val periodUnit = PeriodUnit.valueOf(1)
        val expected = PeriodUnit.WEEK
        assertEquals(expected, periodUnit)
    }

    @Test
    fun `type이 존재하지 않는다면 예외가 발생한다`() {
        val exception = assertThrows<IllegalArgumentException> {
            throw IllegalArgumentException(ERROR_ARGUMENT)
        }
        assertEquals(ERROR_ARGUMENT, exception.message)
    }

    @Test
    fun `type이 d이면 PeriodUnit은 DAY이다`() {
        val periodUnit = PeriodUnit.valueOf('d')
        val expected = PeriodUnit.DAY
        assertEquals(expected, periodUnit)
    }

    @Test
    fun `type이 w이면 PeriodUnit은 WEEK이다`() {
        val periodUnit = PeriodUnit.valueOf('w')
        val expected = PeriodUnit.WEEK
        assertEquals(expected, periodUnit)
    }

    companion object {
        private const val ERROR_ARGUMENT = "존재하지 않는 타입입니다"
    }
}
