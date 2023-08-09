package com.created.team201.presentation.updateStudy

import com.created.domain.model.CreateStudy
import com.created.domain.model.Period
import com.created.domain.model.PeriodUnit

object CreateStudyFixture {
    val study: CreateStudy = CreateStudy(
        name = "자바 스터디",
        peopleCount = 4,
        startDate = "2023.07.30.",
        period = 2,
        cycle = Period(2, PeriodUnit.WEEK),
        introduction = "안녕하세요 자바 스터디입니다. 세부 일정은 첫 스터디 시작 날 서로 상의 후 결정해 나가죠",
    )
}
