package com.created.team201.presentation.updateStudy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.created.domain.repository.StudyDetailRepository
import com.created.domain.repository.UpdateStudyRepository
import com.created.team201.presentation.updateStudy.UpdateStudyViewModel.State.Success
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

class UpdateStudyViewModelTest {
    private lateinit var updateStudyRepository: UpdateStudyRepository
    private lateinit var studyDetailRepository: StudyDetailRepository
    private lateinit var viewModel: UpdateStudyViewModel

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
    fun setupViewModel() {
        studyDetailRepository = mockk()
        updateStudyRepository = mockk()
        viewModel = UpdateStudyViewModel(updateStudyRepository, studyDetailRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `스터디 정보를 갖고 스터디를 개설할 수 있다`() {
        // given
        val studyId = 1L
        coEvery {
            updateStudyRepository.createStudy(
                UpdateStudyFixture.study,
            )
        } returns Result.success(studyId)
        viewModel.setName(UpdateStudyFixture.study.name)
        viewModel.setPeopleCount(UpdateStudyFixture.study.peopleCount)
        viewModel.setStartDate(UpdateStudyFixture.study.startDate)
        viewModel.setPeriod(UpdateStudyFixture.study.period)
        viewModel.setCycle(
            UpdateStudyFixture.study.cycle.date,
            UpdateStudyFixture.study.cycle.unit.type,
        )

        viewModel.setIntroduction(UpdateStudyFixture.study.introduction)

        // when
        viewModel.createStudy()

        // then
        viewModel.studyState.getOrAwaitValue()
        assertEquals(Success(1L), viewModel.studyState.value)
    }

    @Test
    fun `스터디의 필수 정보인 이름, 인원, 시작일, 주기, 회차, 소개가 모두 작성되면 작성 완료 버튼이 활성화 된다`() {
        // given
        viewModel.setName("자바 스터디")
        viewModel.setPeopleCount(3)
        viewModel.setStartDate("2023.07.30")
        viewModel.setPeriod(30)
        viewModel.setCycle(2, 0)
        viewModel.setIntroduction("안녕하세요")

        // when

        // then
        viewModel.isEnableCreateStudy.getOrAwaitValue()
        assertEquals(true, viewModel.isEnableCreateStudy.value)
    }
}
