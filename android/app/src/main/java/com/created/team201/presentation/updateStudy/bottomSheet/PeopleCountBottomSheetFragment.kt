package com.created.team201.presentation.updateStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentPeopleCountBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.updateStudy.UpdateStudyViewModel

class PeopleCountBottomSheetFragment :
    BindingBottomSheetFragment<FragmentPeopleCountBottomSheetBinding>(
        R.layout.fragment_people_count_bottom_sheet,
    ) {
    private val viewModel: UpdateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.onCancelClickListener = { dismiss() }
        binding.onSaveClickListener = ::onSaveButtonClick
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun onSaveButtonClick() {
        viewModel.setPeopleCount(binding.spPeopleCount.value)
        dismiss()
    }
}
