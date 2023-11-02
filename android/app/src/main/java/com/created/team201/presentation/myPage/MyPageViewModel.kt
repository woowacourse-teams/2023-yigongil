package com.created.team201.presentation.myPage

import android.text.InputFilter
import android.text.Spanned
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Nickname
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
import com.created.domain.repository.MyPageRepository
import com.created.team201.presentation.myPage.MyPageViewModel.Event.EnableModify
import com.created.team201.presentation.myPage.model.ProfileType
import com.created.team201.presentation.onBoarding.model.NicknameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

    private val _nicknameState: MutableStateFlow<NicknameState> =
        MutableStateFlow(NicknameState.AVAILABLE)
    val nicknameState: StateFlow<NicknameState>
        get() = _nicknameState.asStateFlow()

    private val _nickname: MutableStateFlow<Nickname> = MutableStateFlow(Nickname("닉네임"))
    val nickname: StateFlow<Nickname>
        get() = _nickname.asStateFlow()

    private val _introduction: MutableStateFlow<String> = MutableStateFlow("")
    val introduction: StateFlow<String>
        get() = _introduction.asStateFlow()

    private val _modifyProfileState: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val modifyProfileState: StateFlow<State>
        get() = _modifyProfileState.asStateFlow()

    private val _myPageEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val myPageEvent: SharedFlow<Event> = _myPageEvent.asSharedFlow()

    init {
        collectRequiredToModifyMyPage()
    }

    private fun collectRequiredToModifyMyPage() {
        viewModelScope.launch {
            combine(
                profileType,
                nickname,
                nicknameState,
                introduction,
            ) { profileType, nickname, nicknameState, introduction ->
                return@combine isInitializeProfileInformation(
                    profileType,
                    nickname,
                    nicknameState,
                    introduction
                )
            }.collect { isEnable ->
                _myPageEvent.emit(EnableModify(isEnable))
            }
        }
    }

    fun resetModifyProfile() {
        profile.value.also {
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
                        _modifyProfileState.value = State.Success
                        _profile.value =
                            profile.value.updateProfileInformation(newProfile.profileInformation)
                    }.onFailure {
                        _modifyProfileState.value = State.Fail
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

    fun changeMyPageEvent(event: Event) {
        viewModelScope.launch {
            _myPageEvent.emit(event)
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

    private fun isInitializeProfileInformation(
        profileType: ProfileType,
        nickname: Nickname,
        nicknameState: NicknameState,
        introduction: String,
    ): Boolean {
        if (profileType == ProfileType.VIEW) {
            return true
        }

        if (
            profileType == ProfileType.MODIFY &&
            nickname.nickname.isBlank().not() &&
            nicknameState == NicknameState.AVAILABLE &&
            introduction.isBlank().not()
        ) {
            return true
        }

        return false
    }

    sealed interface State {
        object Loading : State
        object Success : State
        object Fail : State
    }

    sealed interface Event {
        data class ShowToast(val message: String) : Event
        object ShowDialog : Event
        object ModifyMyPage : Event
        data class EnableModify(val isEnable: Boolean) : Event
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
