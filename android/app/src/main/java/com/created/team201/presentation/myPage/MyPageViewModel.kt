package com.created.team201.presentation.myPage

import android.text.InputFilter
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.model.Nickname
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
import com.created.domain.repository.MyPageRepository
import com.created.team201.data.datasource.remote.MyPageDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.MyPageRepositoryImpl
import com.created.team201.presentation.myPage.model.ProfileInformationUiModel
import com.created.team201.presentation.myPage.model.ProfileType
import com.created.team201.presentation.myPage.model.ProfileUiModel
import com.created.team201.presentation.onBoarding.model.NicknameState
import com.created.team201.presentation.onBoarding.model.NicknameUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import com.created.team201.util.addSourceList
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class MyPageViewModel(
    private val myPageRepository: MyPageRepository,
) : ViewModel() {
    private val _profile: MutableLiveData<ProfileUiModel> = MutableLiveData()
    val profile: LiveData<ProfileUiModel>
        get() = _profile

    private val _profileType: MutableLiveData<ProfileType> = MutableLiveData()
    val profileType: LiveData<ProfileType>
        get() = _profileType

    private val _nicknameState: NonNullMutableLiveData<NicknameState> =
        NonNullMutableLiveData(NicknameState.AVAILABLE)
    val nicknameState: NonNullLiveData<NicknameState>
        get() = _nicknameState

    private val _nickname: NonNullMutableLiveData<NicknameUiModel> = NonNullMutableLiveData(
        NicknameUiModel(""),
    )
    val nickname: NonNullLiveData<NicknameUiModel>
        get() = _nickname

    private val _introduction: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val introduction: NonNullLiveData<String>
        get() = _introduction

    private val _modifyProfileState: MutableLiveData<State> = MutableLiveData()
    val modifyProfileState: LiveData<State>
        get() = _modifyProfileState

    private val _isModifyEnabled: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            addSourceList(profileType, nickname, nicknameState, introduction) {
                isInitializeProfileInformation()
            }
        }
    val isModifyEnabled: LiveData<Boolean>
        get() = _isModifyEnabled

    fun resetModifyProfile() {
        profile.value?.let {
            _nickname.value = it.profileInformation.nickname
            _introduction.value = it.profileInformation.introduction
        }
        _profile.value = profile.value
    }

    fun loadProfile() {
        viewModelScope.launch {
            myPageRepository.getMyPage()
                .onSuccess {
                    _profile.value = it.toUiModel()
                    _nicknameState.value = NicknameState.AVAILABLE
                    _nickname.value = it.profileInformation.nickname.toUiModel()
                    _introduction.value = it.profileInformation.introduction
                }.onFailure { }
        }
    }

    fun patchMyProfile() {
        viewModelScope.launch {
            profile.value?.let {
                val newProfile = it.toDomain().updateProfileInformation(
                    ProfileInformation(nickname.value.toDomain(), introduction.value)
                )
                myPageRepository.patchMyProfile(newProfile.profileInformation)
                    .onSuccess {
                        _modifyProfileState.value = State.SUCCESS
                        _profile.value = profile.value?.run {
                            toDomain().updateProfileInformation(newProfile.profileInformation)
                                .toUiModel()
                        }
                    }.onFailure {
                        _modifyProfileState.value = State.FAIL
                    }

            }
        }
    }

    fun checkAvailableNickname() {
        runCatching {
            if (isSamePreviousNickname(nickname.value.nickname)) {
                _nicknameState.value = NicknameState.AVAILABLE
                return
            }
            nickname.value.toDomain()
        }.onSuccess {
            getAvailableNickname(it)
        }.onFailure {
            _nicknameState.value = NicknameState.UNAVAILABLE
        }
    }

    private fun getAvailableNickname(nickname: Nickname) {
        viewModelScope.launch {
            myPageRepository.getAvailableNickname(nickname)
                .onSuccess { result ->
                    when (result) {
                        false -> _nicknameState.value = NicknameState.AVAILABLE
                        true -> _nicknameState.value = NicknameState.DUPLICATE
                    }
                }
                .onFailure {
                    _nicknameState.value = NicknameState.UNAVAILABLE
                }
        }
    }

    private fun isSamePreviousNickname(currentNickname: String): Boolean =
        profile.value?.profileInformation?.nickname?.nickname == currentNickname

    fun setNickname(nickname: String) {
        if (isSamePreviousNickname(nickname).not()) {
            _nicknameState.value = NicknameState.UNAVAILABLE
        }
        _nickname.value = NicknameUiModel(nickname)
    }

    fun setIntroduction(introduction: String) {
        _introduction.value = introduction
    }

    fun setProfileType(type: ProfileType) {
        _profileType.value = type
    }

    fun switchProfileType() {
        val profileType = profileType.value ?: return
        _profileType.value = when (profileType) {
            ProfileType.VIEW -> ProfileType.MODIFY
            ProfileType.MODIFY -> ProfileType.VIEW
        }
    }

    fun getInputFilter(): Array<InputFilter> = arrayOf(
        object : InputFilter {
            override fun filter(
                text: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned,
                dStart: Int,
                dEnd: Int,
            ): CharSequence {
                if (text.isBlank() || PATTERN_NICKNAME.matcher(text).matches()) {
                    return text
                }

                _nicknameState.value = NicknameState.UNAVAILABLE
                return ""
            }
        },
        InputFilter.LengthFilter(MAX_NICKNAME_LENGTH),
    )

    private fun isInitializeProfileInformation(): Boolean {
        if (profileType.value == ProfileType.VIEW) {
            return true
        }

        if (
            profileType.value == ProfileType.MODIFY &&
            nickname.value.nickname.isBlank().not() &&
            nicknameState.value == NicknameState.AVAILABLE &&
            introduction.value.isBlank().not()
        ) {
            return true
        }

        return false
    }

    sealed interface State {
        object SUCCESS : State
        object FAIL : State
        object IDLE : State
    }

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
            introduction = introduction
        )

    private fun Nickname.toUiModel(): NicknameUiModel = NicknameUiModel(
        nickname = nickname
    )

    private fun ProfileUiModel.toDomain(): Profile = Profile(
        githubId = githubId,
        id = id,
        profileInformation = profileInformation.toDomain(),
        profileImageUrl = profileImageUrl,
        successRate = successRate,
        successfulRoundCount = successfulRoundCount,
        tier = tier,
        tierProgress = tierProgress,
    )

    private fun ProfileInformationUiModel.toDomain(): ProfileInformation =
        ProfileInformation(
            nickname = nickname.toDomain(),
            introduction = introduction
        )

    private fun NicknameUiModel.toDomain(): Nickname = Nickname(
        nickname = nickname
    )

    companion object {
        private val PATTERN_NICKNAME =
            Pattern.compile("^[_a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
        private const val MAX_NICKNAME_LENGTH = 8

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MyPageViewModel(
                    myPageRepository = MyPageRepositoryImpl(
                        MyPageDataSourceImpl(NetworkServiceModule.myPageService),
                    ),
                )
            }
        }
    }
}
