package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentStartDateBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.createStudy.CreateStudyViewModel
import com.created.team201.presentation.createStudy.custom.CalendarChangeListener

class StartDateBottomSheetFragment :
    BindingBottomSheetFragment<FragmentStartDateBottomSheetBinding>(
        R.layout.fragment_start_date_bottom_sheet,
    ) {
    private val viewModel: CreateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setChangeListener()
    }

    private fun setChangeListener() {
        binding.changeListener = object : CalendarChangeListener {
            override fun onChange(date: String) {
                viewModel.startDate.value = date
            }
        }
    }
}
