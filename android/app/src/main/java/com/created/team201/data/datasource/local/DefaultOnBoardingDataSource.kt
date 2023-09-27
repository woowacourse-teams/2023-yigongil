package com.created.team201.data.datasource.local

import javax.inject.Inject

class DefaultOnBoardingDataSource @Inject constructor(
    private val onBoardingStorage: OnBoardingStorage,
) : OnBoardingDataSource {
    override fun getOnBoardingIsDone(): Boolean {
        return onBoardingStorage.fetchIsDone(ONBOARDING_IS_DONE)
    }

    override fun setOnBoardingIsDone(isDone: Boolean) {
        onBoardingStorage.putIsDone(ONBOARDING_IS_DONE, isDone)
    }

    companion object {
        private const val ONBOARDING_IS_DONE = "ONBOARDING_IS_DONE"
    }
}
