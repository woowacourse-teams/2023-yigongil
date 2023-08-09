package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentStartDateBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.createStudy.UpdateStudyViewModel

class StartDateBottomSheetFragment :
    BindingBottomSheetFragment<FragmentStartDateBottomSheetBinding>(
        R.layout.fragment_start_date_bottom_sheet,
    ) {
    private val viewModel: UpdateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        binding.onCancelClickListener = { dismiss() }
        binding.onSaveClickListener = ::onSaveButtonClick
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun onSaveButtonClick() {
        viewModel.setStartDate(binding.calendarCreateStudyStartDate.value)
        dismiss()
    }
}
