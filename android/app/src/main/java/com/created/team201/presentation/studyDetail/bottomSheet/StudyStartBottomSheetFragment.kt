package com.created.team201.presentation.studyDetail.bottomSheet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyStartBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.studyDetail.StudyDetailViewModel

class StudyStartBottomSheetFragment :
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
//        val studyId = arguments?.getLong(KEY_STUDY_ID) ?: INVALID_STUDY_ID
//        validateStudyId(studyId)
//        studyDetailViewModel.startStudy(studyId) ToDo: 서버 연결시 수정
        Toast.makeText(
            context,
            binding.dowsStudyStartBottomSheetDayOfWeekSelector.getSelectedDays().toString(),
            Toast.LENGTH_SHORT,
        ).show()
        dismiss()
    }

    private fun validateStudyId(studyId: Long) {
        if (studyId == INVALID_STUDY_ID) {
            Toast.makeText(
                context,
                getString(R.string.study_start_bottom_sheet_dialog_fragment_not_valid_study),
                Toast.LENGTH_SHORT,
            ).show()
            dismiss()
        }
    }

    companion object {
        private const val KEY_STUDY_ID: String = "KEY_STUDY_ID"
        private const val INVALID_STUDY_ID: Long = 0L

        fun newInstance(studyId: Long): StudyStartBottomSheetFragment =
            StudyStartBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY_STUDY_ID, studyId)
                }
            }
    }
}
