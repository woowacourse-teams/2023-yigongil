package com.created.team201.presentation.onBoarding

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
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
import com.created.domain.model.OnBoarding
import com.created.domain.repository.OnBoardingRepository
import com.created.team201.application.Team201App
import com.created.team201.data.datasource.local.OnBoardingIsDoneDataSourceImpl
import com.created.team201.data.datasource.remote.OnBoardingDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.OnBoardingRepositoryImpl
import com.created.team201.presentation.onBoarding.model.NicknameState
import com.created.team201.presentation.onBoarding.model.NicknameUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import com.created.team201.util.addSourceList
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class OnBoardingViewModel(
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {
    private val _nickname: NonNullMutableLiveData<NicknameUiModel> = NonNullMutableLiveData(
        NicknameUiModel(""),
    )
    val nickname: NonNullLiveData<NicknameUiModel>
        get() = _nickname

    private val _introduction: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val introduction: NonNullLiveData<String>
        get() = _introduction

    private val _nicknameState: MutableLiveData<NicknameState> = MutableLiveData()
    val nicknameState: LiveData<NicknameState>
        get() = _nicknameState

    private val _isEnableSave: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            addSourceList(nickname, nicknameState, introduction) {
                isInitializeOnBoarding()
            }
        }

    private var isSaveOnBoarding: Boolean = false
    private val _onBoardingState: MutableLiveData<State> = MutableLiveData<State>()
    val onBoardingState: LiveData<State>
        get() = _onBoardingState

    val isEnableSave: LiveData<Boolean>
        get() = _isEnableSave

    fun setNickname(nickname: String) {
        _nicknameState.value = NicknameState.UNAVAILABLE
        _nickname.value = NicknameUiModel(nickname)
    }

    fun setIntroduction(introduction: String) {
        _introduction.value = introduction
    }

    fun getAvailableNickname() {
        viewModelScope.launch {
            runCatching {
                nickname.value.toDomain()
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
            OnBoarding(nickname.value.toDomain(), introduction.value).apply {
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
                if (text.isBlank() || PATTERN_NICKNAME.matcher(text).matches()) {
                    return text
                }

                _nicknameState.value = NicknameState.UNAVAILABLE
                return ""
            }
        },
        LengthFilter(MAX_NICKNAME_LENGTH),
    )

    private fun isInitializeOnBoarding(): Boolean =
        nickname.value.nickname.isBlank().not() &&
                nicknameState.value == NicknameState.AVAILABLE &&
                introduction.value.isBlank().not()

    private fun NicknameUiModel.toDomain(): Nickname = Nickname(nickname = nickname)

    companion object {
        private val PATTERN_NICKNAME =
            Pattern.compile("^[_a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
        private const val MAX_NICKNAME_LENGTH = 8

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                OnBoardingViewModel(
                    OnBoardingRepositoryImpl(
                        OnBoardingIsDoneDataSourceImpl(
                            Team201App.provideOnBoardingIsDoneStorage(),
                        ),
                        OnBoardingDataSourceImpl(NetworkServiceModule.onBoardingService),
                    ),
                )
            }
        }
    }
}
