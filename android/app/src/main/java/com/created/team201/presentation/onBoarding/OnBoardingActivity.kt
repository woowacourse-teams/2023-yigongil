package com.created.team201.presentation.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityOnBoardingBinding
import com.created.team201.presentation.MainActivity
import com.created.team201.presentation.common.BindingActivity
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
        setObserveOnBoardingResult()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setObserveOnBoardingResult() {
        viewModel.onBoardingState.observe(this) { state ->
            when (state) {
                is SUCCESS -> {
                    navigateToMain()
                }

                is FAIL -> {
                    showToast(state.message)
                }

                is IDLE -> throw IllegalStateException()
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
        fun getIntent(context: Context): Intent = Intent(context, OnBoardingActivity::class.java)
    }
}
