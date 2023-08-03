package com.created.team201.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.model.FinishedStudy
import com.created.domain.model.Period
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
