package com.created.team201.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.FinishedStudy
import com.created.domain.model.Nickname
import com.created.domain.model.Period
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
import com.created.domain.model.UserProfile
import com.created.team201.data.repository.GuestRepository
import com.created.team201.data.repository.ProfileRepository
import com.created.team201.presentation.myPage.model.ProfileInformationUiModel
import com.created.team201.presentation.myPage.model.ProfileUiModel
import com.created.team201.presentation.onBoarding.model.NicknameUiModel
import com.created.team201.presentation.profile.model.FinishedStudyUiModel
import com.created.team201.presentation.profile.model.UserProfileUiModel
import com.created.team201.presentation.studyList.model.PeriodUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val guestRepository: GuestRepository,
) : ViewModel() {
    private val _profile: MutableLiveData<UserProfileUiModel> = MutableLiveData()
    val profile: LiveData<UserProfileUiModel> get() = _profile

    val isGuest: Boolean get() = guestRepository.getIsGuest()

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
        profileInformation = profileInformation.toUiModel(),
        profileImageUrl = profileImageUrl,
        successRate = successRate,
        successfulRoundCount = successfulRoundCount,
        tier = tier,
        tierProgress = tierProgress,
    )

    private fun ProfileInformation.toUiModel(): ProfileInformationUiModel =
        ProfileInformationUiModel(
            nickname = nickname.toUiModel(),
            introduction = introduction,
        )

    private fun Nickname.toUiModel(): NicknameUiModel = NicknameUiModel(
        nickname = nickname,
    )

    private fun FinishedStudy.toUiModel(): FinishedStudyUiModel = FinishedStudyUiModel(
        id = id,
        name = name,
        averageTier = averageTier,
        numberOfCurrentMembers = numberOfCurrentMembers,
        numberOfMaximumMembers = numberOfMaximumMembers,
        isSucceed = isSucceed,
    )

    private fun Period.toUiModel(): PeriodUiModel = PeriodUiModel(number = date, unit = unit)
}
