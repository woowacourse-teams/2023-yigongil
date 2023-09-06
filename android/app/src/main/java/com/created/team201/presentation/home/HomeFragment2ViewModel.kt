package com.created.team201.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.team201.presentation.home.adapter.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFragment2ViewModel : ViewModel() {
    private val _homeViewState: MutableStateFlow<MutableList<HomeViewState>> = MutableStateFlow(
        mutableListOf()
    )
    val homeViewState: StateFlow<List<HomeViewState>> get() = _homeViewState

    init {
        updateHomeState()
    }

    fun updateHomeState() {
        viewModelScope.launch {
            val fakeData = FakeData()

            _homeViewState.value.addAll(
                listOf(
                    HomeViewState.DashBoard(
                        dDay = fakeData.dashBoard.dDay, mustDo = fakeData.dashBoard.mustDo
                    )
                ) + listOf<HomeViewState.Feed>(
                    HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    ),
                    HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    ),
                    HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    ),
                    HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    ), HomeViewState.Feed(
                        userProfile = fakeData.feeds.userProfile,
                        userName = fakeData.feeds.userName,
                        contentImage = fakeData.feeds.contentImage,
                        content = fakeData.feeds.content,
                        publishedDate = fakeData.feeds.publishedDate,
                    )
                )
            )
        }
    }


    companion object {
        val data = FakeData()
    }
}

class FakeData {
    val dashBoard = FakeDashBoard()
    val feeds = FakeFeed()
}

class FakeDashBoard {
    val dDay: Int = 13
    val mustDo: String = "라면 끓여먹기"
}

class FakeFeed {
    val userProfile: String = "안녕하세요"
    val userName: String = "안녕하세요"
    val contentImage: String = "안녕하세요"
    val content: String = "안녕하세요반강습니다"
    val publishedDate: String = "2023.12.17"
}


