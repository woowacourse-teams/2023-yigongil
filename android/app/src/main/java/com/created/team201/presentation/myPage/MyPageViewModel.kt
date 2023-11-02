package com.created.team201.presentation.myPage

import android.text.InputFilter
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Nickname
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
import com.created.domain.repository.MyPageRepository
import com.created.team201.presentation.myPage.model.ProfileType
import com.created.team201.presentation.onBoarding.model.NicknameState
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import com.created.team201.util.addSourceList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
) : ViewModel() {
    private val _profile: MutableStateFlow<Profile> = MutableStateFlow(DEFAULT_PROFILE)
    val profile: StateFlow<Profile>
        get() = _profile.asStateFlow()

    private val _profileType: MutableStateFlow<ProfileType> = MutableStateFlow(ProfileType.VIEW)
    val profileType: StateFlow<ProfileType>
        get() = _profileType.asStateFlow()

    private val _nicknameState: MutableStateFlow<NicknameState> = MutableStateFlow(NicknameState.AVAILABLE)
    val nicknameState: StateFlow<NicknameState>
        get() = _nicknameState.asStateFlow()

    private val _nickname: MutableStateFlow<Nickname> = MutableStateFlow(Nickname("닉네임"))
    val nickname: StateFlow<Nickname>
        get() = _nickname.asStateFlow()

    private val _introduction: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val introduction: NonNullLiveData<String>
        get() = _introduction

    private val _modifyProfileState: MutableLiveData<State> = MutableLiveData()
    val modifyProfileState: LiveData<State>
        get() = _modifyProfileState

    private val _isModifyEnabled: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            addSourceList(
                profileType.asLiveData(),
                nickname.asLiveData(),
                nicknameState.asLiveData(),
                introduction
            ) {
                isInitializeProfileInformation()
            }
        }
    val isModifyEnabled: LiveData<Boolean>
        get() = _isModifyEnabled

    fun resetModifyProfile() {
        profile.value.let {
            _nickname.value = it.profileInformation.nickname
            _introduction.value = it.profileInformation.introduction
        }
        _profile.value = profile.value
    }

    fun loadProfile() {
        viewModelScope.launch {
            myPageRepository.getMyPage()
                .onSuccess {
                    _profile.value = it
                    _nicknameState.value = NicknameState.AVAILABLE
                    _nickname.value = it.profileInformation.nickname
                    _introduction.value = it.profileInformation.introduction
                }.onFailure { }
        }
    }

    fun patchMyProfile() {
        viewModelScope.launch {
            profile.value.also {
                val newProfile = it.updateProfileInformation(
                    ProfileInformation(nickname.value, introduction.value),
                )
                myPageRepository.patchMyProfile(newProfile.profileInformation)
                    .onSuccess {
                        _modifyProfileState.value = State.SUCCESS
                        _profile.value =
                            profile.value.updateProfileInformation(newProfile.profileInformation)
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
            nickname.value
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
        profile.value.profileInformation.nickname.nickname == currentNickname

    fun setNickname(nickname: String) {
        if (isSamePreviousNickname(nickname).not()) {
            _nicknameState.value = NicknameState.UNAVAILABLE
        }
        _nickname.value = Nickname(nickname)
    }

    fun setIntroduction(introduction: String) {
        _introduction.value = introduction
    }

    fun setProfileType(type: ProfileType) {
        _profileType.value = type
    }

    fun switchProfileType() {
        _profileType.value = when (profileType.value) {
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

    companion object {
        private val PATTERN_NICKNAME =
            Pattern.compile("^[_a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
        private const val MAX_NICKNAME_LENGTH = 8
        private val DEFAULT_PROFILE = Profile(
            githubId = "",
            id = -1L,
            profileInformation = ProfileInformation(Nickname("닉네임"), ""),
            profileImageUrl = null,
            successRate = 0.0,
            successfulRoundCount = 0,
            tier = 1,
            tierProgress = 0,
        )
    }
}
