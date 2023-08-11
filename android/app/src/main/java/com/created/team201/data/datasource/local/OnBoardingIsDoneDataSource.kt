package com.created.team201.data.datasource.local

interface OnBoardingIsDoneDataSource {
    fun getOnBoardingIsDone(): Boolean
    fun setOnBoardingIsDone(isDone: Boolean)
}
