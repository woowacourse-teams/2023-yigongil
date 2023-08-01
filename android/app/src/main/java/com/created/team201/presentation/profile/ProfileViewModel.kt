package com.created.team201.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.model.FinishedStudy
import com.created.domain.model.Period
import com.created.domain.model.PeriodUnit
import com.created.domain.model.Profile
import com.created.domain.model.UserProfile
import com.created.domain.repository.ProfileRepository
import com.created.team201.data.datasource.remote.ProfileDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.ProfileRepositoryImpl
import com.created.team201.presentation.myPage.model.ProfileUiModel
import com.created.team201.presentation.profile.model.FinishedStudyUiModel
import com.created.team201.presentation.profile.model.UserProfileUiModel
import com.created.team201.presentation.studyList.model.PeriodUiModel
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _profile: MutableLiveData<UserProfileUiModel> = MutableLiveData()
    val profile: LiveData<UserProfileUiModel> get() = _profile

    fun initProfile(userId: Long) {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfile(userId)
            }.onSuccess {
                _profile.value = it.toUiModel()
            }
        }
    }

    private fun UserProfile.toUiModel(): UserProfileUiModel = UserProfileUiModel(
        profile = profile.toUiModel(),
        finishedStudies = finishedStudies.map { it.toUiModel() },
    )

    private fun Profile.toUiModel(): ProfileUiModel = ProfileUiModel(
        githubId = githubId,
        id = id,
        introduction = introduction,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        successRate = successRate,
        successfulRoundCount = successfulRoundCount,
        tier = tier,
        tierProgress = tierProgress,
    )

    private fun FinishedStudy.toUiModel(): FinishedStudyUiModel = FinishedStudyUiModel(
        id = id,
        averageTier = averageTier,
        isSucceed = isSucceed,
        name = name,
        numberOfCurrentMembers = numberOfCurrentMembers,
        numberOfMaximumMembers = numberOfMaximumMembers,
        periodOfRound = periodOfRound.toUiModel(),
        startAt = startAt,
        totalRoundCount = totalRoundCount,
    )

    private fun Period.toUiModel(): PeriodUiModel = PeriodUiModel(number = date, unit = unit)

    companion object {

        private val dummy = UserProfileUiModel(
            profile = ProfileUiModel(
                githubId = "no1msh",
                id = 1L,
                introduction = "반달입니다.",
                nickname = "반달",
                profileImageUrl = "https://lh3.googleusercontent.com/ogw/AGvuzYaeOM6cCEBXHw7jg4Hyx9nsp9LRg4P2JMpoTX7LSw=s64-c-mo",
                successRate = 80.6,
                successfulRoundCount = 79,
                tier = 2,
                tierProgress = 70,
            ),
            finishedStudies = listOf(
                FinishedStudyUiModel(
                    id = 1L,
                    averageTier = 2,
                    isSucceed = true,
                    name = "java 스터디",
                    numberOfCurrentMembers = 3,
                    numberOfMaximumMembers = 5,
                    periodOfRound = PeriodUiModel(
                        number = 2,
                        unit = PeriodUnit.DAY,
                    ),
                    startAt = "2023.07.23",
                    totalRoundCount = 20,
                ),
                FinishedStudyUiModel(
                    id = 2L,
                    averageTier = 3,
                    isSucceed = false,
                    name = "kotlin 스터디",
                    numberOfCurrentMembers = 4,
                    numberOfMaximumMembers = 6,
                    periodOfRound = PeriodUiModel(
                        number = 1,
                        unit = PeriodUnit.WEEK,
                    ),
                    startAt = "2023.07.25",
                    totalRoundCount = 30,
                ),
            ),
        )

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ProfileViewModel(
                    profileRepository = ProfileRepositoryImpl(
                        ProfileDataSourceImpl(NetworkServiceModule.profileService),
                    ),
                )
            }
        }
    }
}
