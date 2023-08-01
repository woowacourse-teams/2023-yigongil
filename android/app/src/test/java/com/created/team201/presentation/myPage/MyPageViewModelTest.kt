package com.created.team201.presentation.myPage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.created.domain.model.Profile
import com.created.domain.repository.MyPageRepository
import com.created.team201.presentation.myPage.model.ProfileUiModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyPageViewModelTest {
    private lateinit var viewModel: MyPageViewModel
    private lateinit var repository: MyPageRepository

    // LiveData 테스트를 위한 룰 선언
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    @ExperimentalCoroutinesApi
    fun setupCoroutine() {
        // Dispatcher 상태를 Unconfined 로 변경
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Before
    fun setupRepository() {
        repository = mockk()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `내 정보를 불러올 수 있다`() {
        // given
        coEvery { repository.getMyPage() } answers { Result.success(MyPageFixture.profile) }

        // when
        viewModel = MyPageViewModel(repository)

        // then
        val expected = MyPageFixture.profile.toUiModel()
        assertEquals(expected, viewModel.profile.value)
    }

    private fun Profile.toUiModel(): ProfileUiModel = ProfileUiModel(
        githubId = githubId,
        id = id,
        introduction = introduction,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        successRate = successRate,
        successfulRoundCount = successfulRoundCount,
        tier = tier,
        tierProgress = tierProgress,
    )
}
