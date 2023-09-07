package com.created.team201.presentation.updateStudy.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentThirdUpdateStudyBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.updateStudy.UpdateStudyViewModel

class ThirdUpdateStudyFragment :
    BindingFragment<FragmentThirdUpdateStudyBinding>(R.layout.fragment_third_update_study) {
    private val updateStudyViewModel: UpdateStudyViewModel by activityViewModels {
        UpdateStudyViewModel.Factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        setClickOnDoneButton()
    }

    private fun setClickOnDoneButton() {
        binding.tvThirdUpdateStudyBtnNext.setOnClickListener {
            updateStudyViewModel.updateStudy()
        }
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = updateStudyViewModel
    }
}
