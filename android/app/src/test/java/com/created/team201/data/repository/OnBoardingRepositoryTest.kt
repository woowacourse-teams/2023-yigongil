package com.created.team201.data.repository

import com.created.domain.repository.OnBoardingRepository
import com.created.team201.data.MockServiceModule
import com.created.team201.data.datasource.local.OnBoardingDataSource
import com.created.team201.data.datasource.remote.OnBoardingDataSourceImpl
import com.created.team201.data.remote.api.OnBoardingService
import com.created.team201.presentation.onBoarding.OnBoardingFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OnBoardingRepositoryTest {
    private val service: OnBoardingService = MockServiceModule.onBoardingService
    private lateinit var onBoardingDataSource: OnBoardingDataSource

    private lateinit var repository: OnBoardingRepository

    @Before
    fun setupRepository() {
        onBoardingDataSource = mockk()
        repository =
            DefaultOnBoardingRepository(
                onBoardingIsDoneDataSource = onBoardingDataSource,
                onBoardingDataSource = OnBoardingDataSourceImpl(service)
            )
    }

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

    @ExperimentalCoroutinesApi
    @Test
    fun `내부 저장소에 온보딩 완료 여부가 없거나 false라면 서버에서 온보딩 완료 여부를 확인한다`() = runTest {
        // given
        every {
            onBoardingDataSource.getOnBoardingIsDone()
        } returns false

        val isDone = slot<Boolean>()
        every {
            onBoardingDataSource.setOnBoardingIsDone(capture(isDone))
        } answers { isDone.captured = true }

        // when
        val actual = repository.getIsOnboardingDone()

        // then - true는 온보딩 완료했다는 것을 의미한다
        val expected = true
        assertEquals(true, actual.isSuccess)
        actual.onSuccess {
            assertEquals(expected, it)
        }
    }
}
