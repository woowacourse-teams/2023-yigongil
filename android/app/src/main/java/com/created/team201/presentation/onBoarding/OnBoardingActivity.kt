package com.created.team201.presentation.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.created.team201.R
import com.created.team201.databinding.ActivityOnBoardingBinding
import com.created.team201.presentation.main.MainActivity
import com.created.team201.presentation.onBoarding.OnBoardingViewModel.Event.EnableSave
import com.created.team201.presentation.onBoarding.OnBoardingViewModel.Event.SaveOnBoarding
import com.created.team201.presentation.onBoarding.OnBoardingViewModel.Event.ShowToast
import com.created.team201.util.collectOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private val viewModel: OnBoardingViewModel by viewModels()
    private val binding: ActivityOnBoardingBinding by lazy {
        ActivityOnBoardingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setNicknameInputFilter()
        setInputTextChanged()
        setSaveOnBoardingEvent()
        collectOnBoardingEvent()
        collectNicknameState()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == ACTION_DOWN && currentFocus is EditText
        ) {
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            currentFocus?.clearFocus()
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun setNicknameInputFilter() {
        binding.etOnBoardingNickname.filters = viewModel.getInputFilter()
    }

    private fun setInputTextChanged() {
        binding.etOnBoardingNickname.doOnTextChanged { text, _, _, _ ->
            viewModel.setNickname(text.toString())
        }

        binding.etOnBoardingIntroduction.doOnTextChanged { text, _, _, _ ->
            viewModel.setIntroduction(text.toString())
        }
    }

    private fun setSaveOnBoardingEvent() {
        binding.tvOnBoardingBtnSave.setOnClickListener {
            viewModel.patchOnBoarding()
        }
    }

    private fun collectOnBoardingEvent() {
        viewModel.onBoardingEvent.collectOnStarted(this) { event ->
            when (event) {
                ShowToast -> showToast(getString(R.string.onBoarding_toast_fail))
                SaveOnBoarding -> navigateToMain()
                is EnableSave -> binding.tvOnBoardingBtnSave.isEnabled = event.isEnable
            }
        }
    }

    private fun collectNicknameState() {
        viewModel.nicknameState.collectOnStarted(this) { nicknameState ->
            binding.tvOnBoardingNicknameValidateIntroduction.text = getString(nicknameState.introduction)
            binding.tvOnBoardingNicknameValidateIntroduction.setTextColor(getColor(nicknameState.color))
        }
    }

    private fun navigateToMain() {
        startActivity(MainActivity.getIntent(this))
        finish()
    }

    private fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, OnBoardingActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
