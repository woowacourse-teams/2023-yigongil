package com.created.team201.presentation.createStudy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.created.team201.R
import com.created.team201.databinding.FragmentSecondCreateStudyBinding
import com.created.team201.presentation.common.BindingFragment
import kotlinx.coroutines.launch

class SecondCreateStudyFragment :
    BindingFragment<FragmentSecondCreateStudyBinding>(R.layout.fragment_second_create_study) {
    private val createStudyViewModel: CreateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBinding()
        setupCollectEnableNext()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = createStudyViewModel
    }

    private fun setupCollectEnableNext() {
        lifecycleScope.launch {
            createStudyViewModel.isEnableSecondCreateStudyNext.collect { isEnable ->
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    binding.tvSecondCreateStudyBtnNext.isEnabled = isEnable
                }
            }
        }
    }
}
