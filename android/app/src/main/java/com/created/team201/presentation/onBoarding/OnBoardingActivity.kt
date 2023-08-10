package com.created.team201.presentation.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
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
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, OnBoardingActivity::class.java)
    }
}
