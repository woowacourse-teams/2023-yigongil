package com.created.team201.presentation.createStudy

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.created.team201.databinding.FragmentSecondCreateStudyBinding
import com.created.team201.presentation.common.BindingViewFragment
import kotlinx.coroutines.launch

class SecondCreateStudyFragment :
    BindingViewFragment<FragmentSecondCreateStudyBinding>(FragmentSecondCreateStudyBinding::inflate) {
    private val createStudyViewModel: CreateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupStudyNameTitleChanged()
        setupStudyIntroductionChanged()
        setupCollectEnableNext()
    }

    private fun setupStudyNameTitleChanged() {
        binding.tvSecondCreateStudyStudyNameTitle.doOnTextChanged { text, _, _, _ ->
            createStudyViewModel.setStudyName(text.toString())
        }
    }

    private fun setupStudyIntroductionChanged() {
        binding.etSecondCreateStudyStudyIntroduction.doOnTextChanged { text, _, _, _ ->
            createStudyViewModel.setStudyIntroduction(text.toString())
        }
    }

    private fun setupCollectEnableNext() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createStudyViewModel.isEnableSecondCreateStudyNext.collect { isEnable ->
                    binding.tvSecondCreateStudyBtnNext.isEnabled = isEnable
                }
            }
        }
    }
}
