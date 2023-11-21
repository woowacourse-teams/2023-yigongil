package com.created.team201.presentation.createStudy

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.created.team201.databinding.FragmentSecondCreateStudyBinding
import com.created.team201.presentation.common.BindingViewFragment
import com.created.team201.util.collectOnStarted

class SecondCreateStudyFragment :
    BindingViewFragment<FragmentSecondCreateStudyBinding>(FragmentSecondCreateStudyBinding::inflate) {
    private val createStudyViewModel: CreateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupStudyNameTitleChanged()
        setupStudyIntroductionChanged()
        setupNextClick()
        collectEnableNext()
    }

    private fun setupStudyNameTitleChanged() {
        binding.etSecondCreateStudyStudyName.doOnTextChanged { text, _, _, _ ->
            createStudyViewModel.setStudyName(text.toString())
        }
    }

    private fun setupStudyIntroductionChanged() {
        binding.etSecondCreateStudyStudyIntroduction.doOnTextChanged { text, _, _, _ ->
            createStudyViewModel.setStudyIntroduction(text.toString())
        }
    }

    private fun setupNextClick() {
        binding.tvSecondCreateStudyBtnNext.setOnClickListener {
            createStudyViewModel.createStudy()
        }
    }

    private fun collectEnableNext() {
        createStudyViewModel.isEnableSecondCreateStudyNext.collectOnStarted(viewLifecycleOwner) { isEnable ->
            binding.tvSecondCreateStudyBtnNext.isEnabled = isEnable
        }
    }
}
