package com.created.team201.presentation.studyDetail.bottomSheet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyStartBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.studyDetail.StudyDetailViewModel

class StudyStartBottomSheetFragment(private val studyId: Long) :
    BindingBottomSheetFragment<FragmentStudyStartBottomSheetBinding>(
        R.layout.fragment_study_start_bottom_sheet,
    ) {
    private val studyDetailViewModel: StudyDetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        setupDayOnClickListener()
    }

    private fun initBinding() {
        binding.viewModel = studyDetailViewModel
        binding.onCancelClickListener = { dismiss() }
        binding.onStartClickListener = ::onStartButtonClick
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupDayOnClickListener() {
        binding.dowsStudyStartBottomSheetDayOfWeekSelector.setDayOnClickListener {
            val selectedDaysSize: Int =
                binding.dowsStudyStartBottomSheetDayOfWeekSelector.getSelectedDaysSize()

            if (selectedDaysSize > 0) {
                binding.tvStudyStartBottomSheetBtnStart.isEnabled = true
                return@setDayOnClickListener
            }
            binding.tvStudyStartBottomSheetBtnStart.isEnabled = false
        }
    }

    private fun onStartButtonClick() {
        // studyDetailViewModel.startStudy(studyId) ToDo: 서버 연결 필요
        Toast.makeText(
            context,
            binding.dowsStudyStartBottomSheetDayOfWeekSelector.getSelectedDays().toString(),
            Toast.LENGTH_SHORT,
        ).show()
        dismiss()
    }
}
