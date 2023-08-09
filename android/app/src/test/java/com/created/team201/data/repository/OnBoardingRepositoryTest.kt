package com.created.team201.data.repository

import com.created.domain.repository.OnBoardingRepository
import com.created.team201.data.MockServiceModule
import com.created.team201.data.datasource.remote.OnBoardingDataSourceImpl
import com.created.team201.data.remote.api.OnBoardingService
import com.created.team201.presentation.onBoarding.OnBoardingFixture
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OnBoardingRepositoryTest {
    private val service: OnBoardingService = MockServiceModule.onBoardingService

    private val repository: OnBoardingRepository =
        OnBoardingRepositoryImpl(OnBoardingDataSourceImpl(service))

    @ExperimentalCoroutinesApi
    @Test
    fun `닉네임 중복 검사 후 닉네임 사용 가능하다`() = runTest {
        // given

        // when
        val actual = repository.getAvailableNickname(OnBoardingFixture.nickname)

        // then - false는 닉네임이 중복되지 않는다
        val expected = false
        assertEquals(true, actual.isSuccess)
        actual.onSuccess {
            assertEquals(expected, it)
        }
    }
}
