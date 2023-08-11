package com.created.team201.presentation.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.created.domain.repository.SettingRepository
import com.created.team201.presentation.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingViewModelTest {
    private lateinit var repository: SettingRepository
    private lateinit var viewModel: SettingViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    @ExperimentalCoroutinesApi
    fun setupCoroutine() {
        // Dispatcher 상태를 Unconfined 로 변경
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Before
    fun setupViewModel() {
        repository = mockk()
        viewModel = SettingViewModel(repository)
    }

    @Test
    fun `로그아웃 시 내부 저장소에 저장된 token 정보를 삭제한다`() {
        // given
        justRun {
            repository.logout()
        }

        // when
        viewModel.logout()

        // then
        verify { repository.logout() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `회원 탈퇴를 할 수 있다`() {
        // given
        coEvery {
            repository.requestWithdrawAccount()
        } returns Result.success(Unit)

        // when
        viewModel.withdrawAccount()

        // then
        viewModel.isWithdrawAccountState.getOrAwaitValue()
        assertEquals(SettingViewModel.State.SUCCESS, viewModel.isWithdrawAccountState.value)
    }
}
