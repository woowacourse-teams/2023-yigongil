package com.created.team201.presentation.home

import com.created.team201.presentation.home.model.StudyUiModel
import com.created.team201.presentation.home.model.TodoUiModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StudyUiModelTest {

    @Test
    fun testGrassCount() {
        // given: 스터디원 필수투두 진행률 20%
        val progressRate = 20
        val studyUiModel = STUB_DATA.copy(progressRate = progressRate)

        // when: StudyUiModel의 grass 프로퍼티를 호출할 때
        val actual = studyUiModel.grass
        val expected = progressRate / GRASS_RATE_PER

        // then: [진행률 -> 잔디 갯수] 환산 함수 호출 및 결과값 출력
        assertEquals(expected, actual.count { it.grassState })
    }

    companion object {
        private const val GRASS_RATE_PER = 100 / 9
        private val STUB_DATA = StudyUiModel(
            "빨리 만들자",
            0,
            5,
            "2023.01.02",
            TodoUiModel(2, "잔디구현끝내기", true),
            listOf(
                TodoUiModel(3, "홈뷰 끝내기", false),
                TodoUiModel(4, "홈뷰 끝내기2", false),
                TodoUiModel(5, "홈뷰 끝내기3", false),
                TodoUiModel(6, "홈뷰 끝내기4", false),
            ),
        )
    }
}
