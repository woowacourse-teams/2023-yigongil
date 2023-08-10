package com.created.team201.presentation.onBoarding

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.team201.presentation.onBoarding.model.NicknameState
import com.created.team201.presentation.onBoarding.model.NicknameUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import com.created.team201.util.addSourceList
import java.util.regex.Pattern

class OnBoardingViewModel : ViewModel() {
    private val _nickname: MutableLiveData<NicknameUiModel> = MutableLiveData()
    val nickname: LiveData<NicknameUiModel>
        get() = _nickname

    private val _introduction: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val introduction: NonNullLiveData<String>
        get() = _introduction

    private val _nicknameState: MutableLiveData<NicknameState> = MutableLiveData()
    val nicknameState: LiveData<NicknameState>
        get() = _nicknameState

    private val _isEnableSave: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            addSourceList(nickname, nicknameState) {
                isInitializeOnBoarding()
            }
        }
    val isEnableSave: LiveData<Boolean>
        get() = _isEnableSave

    fun setNickname(nickname: String) {
        _nickname.value = NicknameUiModel(nickname)
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
        LengthFilter(MAX_NICKNAME_LENGTH),
    )

    private fun isInitializeOnBoarding(): Boolean =
        nickname.value != null && nicknameState.value == NicknameState.AVAILABLE

    companion object {
        private val PATTERN_NICKNAME = Pattern.compile("^[_a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]+$")
        private const val MAX_NICKNAME_LENGTH = 8

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                OnBoardingViewModel()
            }
        }
    }
}
