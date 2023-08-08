package com.created.team201.presentation.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter.LengthFilter
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.created.team201.R
import com.created.team201.databinding.ActivityOnBoardingBinding
import com.created.team201.presentation.common.BindingActivity

class OnBoardingActivity :
    BindingActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {
    private val viewModel: OnBoardingViewModel by viewModels {
        OnBoardingViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initNickname()
        setObserveNickname()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initNickname() {
        binding.etOnBoardingNickname.filters =
            arrayOf(viewModel.getInputFilter(), LengthFilter(MAX_NICKNAME_LENGTH))
    }

    private fun setObserveNickname() {
        viewModel.nicknameState.observe(this) { state ->
            setNicknameValidateIntroduction(state.color, state.introduction)
        }
    }

    private fun setNicknameValidateIntroduction(
        @ColorRes color: Int, @StringRes text: Int
    ) {
        binding.tvOnBoardingNicknameValidateIntroduction.setTextColor(getColor(color))
        binding.tvOnBoardingNicknameValidateIntroduction.text = getString(text)
    }

    companion object {
        private const val MAX_NICKNAME_LENGTH = 8

        fun getIntent(context: Context): Intent = Intent(context, OnBoardingActivity::class.java)
    }
}
