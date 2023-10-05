package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyDateBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.createStudy.CreateStudyViewModel

class StudyDateBottomSheetFragment :
    BindingBottomSheetFragment<FragmentStudyDateBottomSheetBinding>(
        R.layout.fragment_study_date_bottom_sheet,
    ) {
    private val createStudyViewModel: CreateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        binding.viewModel = createStudyViewModel
        binding.onCancelClickListener = { dismiss() }
        binding.onSaveClickListener = ::onSaveButtonClick
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun onSaveButtonClick() {
        createStudyViewModel.setStudyDate(binding.spStudyDate.value)
        dismiss()
    }
}
