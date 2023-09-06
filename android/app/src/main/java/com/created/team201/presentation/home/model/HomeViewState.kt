package com.created.team201.presentation.home.model

sealed interface HomeViewState {
    data class Feed(
        val userProfile: String,
        val userName: String,
        val contentImage: String,
        val content: String,
        val publishedDate: String,
    ) : HomeViewState

    data class DashBoard(
        val dDay: Int,
        val mustDo: String
    ) : HomeViewState
}