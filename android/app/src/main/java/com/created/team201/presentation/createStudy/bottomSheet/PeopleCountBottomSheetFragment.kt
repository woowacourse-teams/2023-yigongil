package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentPeopleCountBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.createStudy.CreateStudyViewModel

class PeopleCountBottomSheetFragment :
    BindingBottomSheetFragment<FragmentPeopleCountBottomSheetBinding>(
        R.layout.fragment_people_count_bottom_sheet,
    ) {
    private val viewModel: CreateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.bottomSheetFragment = this
    }

    fun onSaveButtonClick() {
        viewModel.peopleCount.value = binding.peopleCountPickerCreateStudyPeopleCount.value
        dismiss()
    }
}
