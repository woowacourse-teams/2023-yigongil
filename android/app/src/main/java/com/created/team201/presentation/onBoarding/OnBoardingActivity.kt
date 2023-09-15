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
import com.created.team201.R
import com.created.team201.databinding.ActivityOnBoardingBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.main.MainActivity
import com.created.team201.presentation.onBoarding.OnBoardingViewModel.State.FAIL
import com.created.team201.presentation.onBoarding.OnBoardingViewModel.State.IDLE
import com.created.team201.presentation.onBoarding.OnBoardingViewModel.State.SUCCESS

class OnBoardingActivity :
    BindingActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {
    private val viewModel: OnBoardingViewModel by viewModels {
        OnBoardingViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setValidateEditText()
        setObserveOnBoardingResult()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setValidateEditText() {
        binding.etOnBoardingNickname.setOnFocusChangeListener { _, focus ->
            if (focus) return@setOnFocusChangeListener
            viewModel.getAvailableNickname()
        }
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

    private fun setObserveOnBoardingResult() {
        viewModel.onBoardingState.observe(this) { state ->
            when (state) {
                SUCCESS -> {
                    navigateToMain()
                }

                FAIL -> {
                    showToast(getString(R.string.onBoarding_toast_fail))
                }

                IDLE -> throw IllegalStateException()
            }
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
                flags = Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
