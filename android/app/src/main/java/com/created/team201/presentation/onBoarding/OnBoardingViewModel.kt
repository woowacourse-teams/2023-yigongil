package com.created.team201.presentation.onBoarding

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.Spanned
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Nickname
import com.created.domain.model.OnBoarding
import com.created.domain.repository.OnBoardingRepository
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
class OnBoardingViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {
    private val _nickname: MutableStateFlow<String> = MutableStateFlow("")
    val nickname: StateFlow<String> get() = _nickname.asStateFlow()

    private val _introduction: MutableStateFlow<String> = MutableStateFlow("")

    private val _nicknameState: MutableStateFlow<NicknameState> =
        MutableStateFlow(NicknameState.UNAVAILABLE)
    val nicknameState: StateFlow<NicknameState> get() = _nicknameState.asStateFlow()

    private val _onBoardingEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val onBoardingEvent: SharedFlow<Event> get() = _onBoardingEvent.asSharedFlow()

    init {
        initValidateNicknameDebounce()
        collectRequiredOnBoarding()
    }

    private fun collectRequiredOnBoarding() {
        viewModelScope.launch {
            combine(
                nickname,
                nicknameState,
                _introduction
            ) { nickname, nicknameState, introduction ->
                return@combine isInitializeOnBoarding(nickname, nicknameState, introduction)
            }.collect { isEnable ->
                _onBoardingEvent.emit(Event.EnableSave(isEnable))
            }
        }
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    fun setIntroduction(introduction: String) {
        _introduction.value = introduction
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
        viewModelScope.launch {
            runCatching {
                Nickname(nickname)
            }.onSuccess {
                onBoardingRepository.getAvailableNickname(it)
                    .onSuccess { result ->
                        when (result) {
                            false -> _nicknameState.value = NicknameState.AVAILABLE
                            true -> _nicknameState.value = NicknameState.DUPLICATE
                        }
                    }
                    .onFailure {
                        _nicknameState.value = NicknameState.UNAVAILABLE
                    }
            }.onFailure {
                _nicknameState.value = NicknameState.UNAVAILABLE
            }
        }
    }

    fun patchOnBoarding() {
        viewModelScope.launch {
            runCatching {
                Nickname(nickname.value)
            }.onSuccess { nickname ->
                OnBoarding(nickname, _introduction.value).apply {
                    onBoardingRepository.patchOnBoarding(this)
                        .onSuccess {
                            _onBoardingEvent.emit(Event.SaveOnBoarding)
                        }.onFailure {
                            _onBoardingEvent.emit(Event.ShowToast)
                        }
                }
            }
        }
    }

    sealed interface Event {
        object ShowToast : Event
        object SaveOnBoarding : Event
        data class EnableSave(val isEnable: Boolean) : Event
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
                if (text.toString() != nickname.value) {
                    _nicknameState.value = NicknameState.UNAVAILABLE
                }

                if (text.isBlank() || PATTERN_NICKNAME.matcher(text).matches()) {
                    return text
                }

                return ""
            }
        },
        LengthFilter(MAX_NICKNAME_LENGTH),
    )

    private fun isInitializeOnBoarding(
        nickname: String,
        nicknameState: NicknameState,
        introduction: String,
    ): Boolean =
        nickname.isBlank().not() &&
                nicknameState == NicknameState.AVAILABLE &&
                introduction.isBlank().not()

    companion object {
        private val PATTERN_NICKNAME =
            Pattern.compile("^[_a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
        private const val MAX_NICKNAME_LENGTH = 8
    }
}
