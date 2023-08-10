package com.created.team201.data.repository

import com.created.domain.repository.UpdateStudyRepository
import com.created.team201.data.MockServiceModule
import com.created.team201.data.datasource.remote.UpdateStudyDataSourceImpl
import com.created.team201.data.remote.api.UpdateStudyService
import com.created.team201.presentation.updateStudy.UpdateStudyFixture
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateStudyRepositoryTest {
    private val service: UpdateStudyService = MockServiceModule.updateStudyService

    private val repository: UpdateStudyRepository =
        UpdateStudyRepositoryImpl(UpdateStudyDataSourceImpl(service))

    @ExperimentalCoroutinesApi
    @Test
    fun `스터디를 성공적으로 생성한다`() = runTest {
        // given

        // when
        val actual = repository.createStudy(UpdateStudyFixture.study)

        // then
        assertEquals(true, actual.isSuccess)
        actual.onSuccess { studyId ->
            assertEquals(1L, studyId)
        }
    }
}
