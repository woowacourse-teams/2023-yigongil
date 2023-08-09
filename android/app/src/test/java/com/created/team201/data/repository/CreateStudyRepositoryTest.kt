package com.created.team201.data.repository

import com.created.domain.repository.CreateStudyRepository
import com.created.team201.data.MockServiceModule
import com.created.team201.data.datasource.remote.CreateStudyDataSourceImpl
import com.created.team201.data.remote.api.CreateStudyService
import com.created.team201.presentation.updateStudy.CreateStudyFixture
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CreateStudyRepositoryTest {
    private val service: CreateStudyService = MockServiceModule.createStudyService

    private val repository: CreateStudyRepository =
        CreateStudyRepositoryImpl(CreateStudyDataSourceImpl(service))

    @ExperimentalCoroutinesApi
    @Test
    fun `스터디를 성공적으로 생성한다`() = runTest {
        // given

        // when
        val actual = repository.createStudy(CreateStudyFixture.study)

        // then
        assertEquals(true, actual.isSuccess)
        actual.onSuccess { studyId ->
            assertEquals(1L, studyId)
        }
    }
}
