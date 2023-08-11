package com.created.team201.data.datasource.local

class OnBoardingIsDoneDataSourceImpl(
    private val onBoardingIsDoneStorage: OnBoardingIsDoneStorage,
) : OnBoardingIsDoneDataSource {
    override fun getOnBoardingIsDone(): Boolean {
        return onBoardingIsDoneStorage.fetchIsDone(ONBOARDING_IS_DONE)
    }

    override fun setOnBoardingIsDone(isDone: Boolean) {
        onBoardingIsDoneStorage.putIsDone(ONBOARDING_IS_DONE, isDone)
    }

    companion object {
        private const val ONBOARDING_IS_DONE = "ONBOARDING_IS_DONE"
    }
}
