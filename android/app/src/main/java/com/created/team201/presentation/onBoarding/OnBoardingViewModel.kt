package com.created.team201.presentation.onBoarding

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Nickname
import com.created.domain.model.OnBoarding
import com.created.domain.repository.OnBoardingRepository
import com.created.team201.presentation.onBoarding.model.NicknameState
import com.created.team201.presentation.onBoarding.model.NicknameUiModel
import com.created.team201.util.addSourceList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _isEnableSave: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            addSourceList(
                _nickname.asLiveData(),
                nicknameState.asLiveData(),
                _introduction.asLiveData()
            ) {
                isInitializeOnBoarding()
            }
        }

    private var isSaveOnBoarding: Boolean = false
    private val _onBoardingState: MutableLiveData<State> = MutableLiveData<State>()
    val onBoardingState: LiveData<State>
        get() = _onBoardingState

    val isEnableSave: LiveData<Boolean>
        get() = _isEnableSave

    init {
        initValidateNicknameDebounce()
    }

    fun setNickname(nickname: CharSequence) {
        viewModelScope.launch {
            _nickname.value = nickname.toString()
        }
    }

    fun setIntroduction(introduction: String) {
        _introduction.value = introduction
    }

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
        if (isSaveOnBoarding) return
        isSaveOnBoarding = true

        viewModelScope.launch {
            OnBoarding(Nickname(nickname.value), _introduction.value).apply {
                onBoardingRepository.patchOnBoarding(this)
                    .onSuccess {
                        _onBoardingState.value = State.SUCCESS
                    }.onFailure {
                        _onBoardingState.value = State.FAIL
                        isSaveOnBoarding = false
                    }
            }
        }
    }

    sealed interface State {
        object SUCCESS : State
        object FAIL : State
        object IDLE : State
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

    private fun isInitializeOnBoarding(): Boolean =
        _nickname.value.isBlank().not() &&
                nicknameState.value == NicknameState.AVAILABLE &&
                _introduction.value.isBlank().not()

    private fun NicknameUiModel.toDomain(): Nickname = Nickname(nickname = nickname)

    companion object {
        private val PATTERN_NICKNAME =
            Pattern.compile("^[_a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
        private const val MAX_NICKNAME_LENGTH = 8
    }
}
