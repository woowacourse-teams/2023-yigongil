package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentStartDateBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.createStudy.CreateStudyViewModel

class StartDateBottomSheetFragment :
    BindingBottomSheetFragment<FragmentStartDateBottomSheetBinding>(
        R.layout.fragment_start_date_bottom_sheet,
    ) {
    private val viewModel: CreateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        binding.bottomSheetFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    fun onSaveButtonClick() {
        viewModel.startDate.value = binding.calendarCreateStudyStartDate.value
        dismiss()
    }
}
