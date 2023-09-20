package com.created.team201.data.repository

import com.created.domain.repository.MyPageRepository
import com.created.team201.data.MockServiceModule
import com.created.team201.data.datasource.remote.MyPageDataSourceImpl
import com.created.team201.data.remote.api.MyPageService
import com.created.team201.presentation.myPage.MyPageFixture
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MyPageRepositoryTest {
    private val service: MyPageService = MockServiceModule.myPageService

    private val repository: MyPageRepository =
        DefaultMyPageRepository(MyPageDataSourceImpl(service))

    @ExperimentalCoroutinesApi
    @Test
    fun `마이페이지 정보를 성공적으로 가져온다`() = runTest {
        // given

        // when
        val actual = repository.getMyPage()

        // then
        val expected = MyPageFixture.profile

        assertEquals(true, actual.isSuccess)
        actual.onSuccess {
            assertEquals(expected, it)
        }
    }
}
