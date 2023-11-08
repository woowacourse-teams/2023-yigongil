package com.created.team201.presentation.myPage

import android.text.InputFilter
import android.text.Spanned
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Nickname
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
import com.created.team201.data.repository.MyPageRepository
import com.created.team201.presentation.myPage.MyPageViewModel.Event.EnableModify
import com.created.team201.presentation.onBoarding.model.NicknameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _nicknameState: MutableStateFlow<NicknameState> =
        MutableStateFlow(NicknameState.AVAILABLE)
    val nicknameState: StateFlow<NicknameState>
        get() = _nicknameState.asStateFlow()

    private val _nickname: MutableStateFlow<String> = MutableStateFlow("")
    val nickname: StateFlow<String>
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
        initValidateNicknameDebounce()
        collectRequiredToModifyMyPage()
    }

    @OptIn(FlowPreview::class)
    private fun initValidateNicknameDebounce() {
        viewModelScope.launch {
            _nickname.debounce(700)
                .filter { text -> text.isNotEmpty() }
                .onEach {
                    validateNickname(nickname.value)
                }.launchIn(this@launch)
        }
    }

    private fun validateNickname(nickname: String) {
        runCatching {
            if (isSamePreviousNickname(nickname)) {
                _nicknameState.value = NicknameState.AVAILABLE
                return
            }
            Nickname(nickname)
        }.onSuccess {
            getAvailableNickname(it)
        }.onFailure {
            _nicknameState.value = NicknameState.UNAVAILABLE
        }
    }

    private fun collectRequiredToModifyMyPage() {
        viewModelScope.launch {
            combine(
                nickname,
                nicknameState,
                introduction,
            ) { nickname, nicknameState, introduction ->
                return@combine isInitializeProfileInformation(
                    nickname,
                    nicknameState,
                    introduction
                )
            }.collect { isEnable ->
                _myPageEvent.emit(EnableModify(isEnable))
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            myPageRepository.getMyPage()
                .onSuccess {
                    _profile.value = it
                    _nicknameState.value = NicknameState.AVAILABLE
                    _nickname.value = it.profileInformation.nickname.nickname
                    _introduction.value = it.profileInformation.introduction
                }.onFailure { }
        }
    }

    fun patchMyProfile() {
        viewModelScope.launch {
            runCatching {
                Nickname(nickname.value)
            }.onSuccess {
                val newProfile = profile.value.updateProfileInformation(
                    ProfileInformation(Nickname(nickname.value), introduction.value),
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

    fun changeMyPageEvent(event: Event) {
        viewModelScope.launch {
            _myPageEvent.emit(event)
        }
    }

    fun setMyPageEvent(event: Event) {
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
        _nickname.value = nickname
    }

    fun setIntroduction(introduction: String) {
        _introduction.value = introduction
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
        nickname: String,
        nicknameState: NicknameState,
        introduction: String,
    ): Boolean {

        if (
            nickname.isBlank().not() &&
            nicknameState == NicknameState.AVAILABLE &&
            introduction.isBlank().not()
        ) {
            return true
        }

        return false
    }

    enum class State {
        Loading, Success, Fail
    }

    sealed interface Event {
        data class ShowToast(val message: String) : Event
        object NavigateToSetting : Event
        object NavigateToModify : Event
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
