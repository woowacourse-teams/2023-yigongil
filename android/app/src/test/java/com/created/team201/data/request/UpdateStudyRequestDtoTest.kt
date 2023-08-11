package com.created.team201.data.request

import com.created.domain.model.Period
import com.created.domain.model.PeriodUnit
import com.created.team201.data.remote.request.UpdateStudyRequestDto
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UpdateStudyRequestDtoTest {
    @Test
    fun `Period의 date는 14, type은 PeriodUnit_DAY 이면 PeriodString은 14d이다`() {
        // given
        val period = Period(14, PeriodUnit.DAY)

        // when
        val actual = UpdateStudyRequestDto.getPeriodString(period)

        // then
        assertEquals("14d", actual)
    }

    @Test
    fun `Period의 date는 2, type은 PeriodUnit_WEEK 이면 PeriodString은 2w이다`() {
        // given
        val period = Period(2, PeriodUnit.WEEK)

        // when
        val actual = UpdateStudyRequestDto.getPeriodString(period)

        // then
        assertEquals("2w", actual)
    }
}
