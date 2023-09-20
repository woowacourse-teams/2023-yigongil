package com.created.team201.data.datasource.local

interface OnBoardingDataSource {
    fun getOnBoardingIsDone(): Boolean
    fun setOnBoardingIsDone(isDone: Boolean)
}
