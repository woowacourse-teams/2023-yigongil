package com.created.team201.presentation.onBoarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.created.domain.repository.OnBoardingRepository
import com.created.team201.presentation.onBoarding.OnBoardingViewModel.State.SUCCESS
import com.created.team201.presentation.onBoarding.model.NicknameState.AVAILABLE
import com.created.team201.presentation.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OnBoardingViewModelTest {
    private lateinit var repository: OnBoardingRepository
    private lateinit var viewModel: OnBoardingViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    @ExperimentalCoroutinesApi
    fun setupCoroutine() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Before
    fun setupViewModel() {
        repository = mockk()
        viewModel = OnBoardingViewModel(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `닉네임 중복을 검사할 수 있다`() {
        // given
        viewModel.setNickname(OnBoardingFixture.nickname.nickname)

        coEvery {
            repository.getAvailableNickname(OnBoardingFixture.nickname)
        } returns Result.success(false)

        // when
        viewModel.getAvailableNickname()

        // then
        viewModel.nicknameState.getOrAwaitValue()
        assertEquals(AVAILABLE, viewModel.nicknameState.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `닉네임과 소개 정보를 갖고 내 정보를 설정할 수 있다`() {
        // given
        viewModel.setNickname(OnBoardingFixture.nickname.nickname)
        viewModel.setIntroduction(OnBoardingFixture.introduction)

        coEvery {
            repository.getAvailableNickname(OnBoardingFixture.nickname)
        } returns Result.success(false)

        coEvery {
            repository.patchOnBoarding(OnBoardingFixture.onBoarding)
        } returns Result.success(Unit)

        viewModel.getAvailableNickname()

        // when
        viewModel.patchOnBoarding()

        // then
        viewModel.nicknameState.getOrAwaitValue()
        viewModel.onBoardingState.getOrAwaitValue()
        assertEquals(AVAILABLE, viewModel.nicknameState.value)
        assertEquals(SUCCESS, viewModel.onBoardingState.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `기본 작성완료 버튼은 비활성화 상태다`() {
        // given

        // when

        // then
        viewModel.isEnableSave.getOrAwaitValue()
        assertEquals(false, viewModel.isEnableSave.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `닉네임이 공백이 아니고, 닉네임 중복 검증을 통과하면 버튼이 활성화 된다`() {
        // given
        viewModel.setNickname(OnBoardingFixture.nickname.nickname)

        coEvery {
            repository.getAvailableNickname(OnBoardingFixture.nickname)
        } returns Result.success(false)

        // when
        viewModel.getAvailableNickname()

        // then
        viewModel.isEnableSave.getOrAwaitValue()
        viewModel.nicknameState.getOrAwaitValue()
        assertEquals(AVAILABLE, viewModel.nicknameState.value)
        assertEquals(true, viewModel.isEnableSave.value)
    }
}
