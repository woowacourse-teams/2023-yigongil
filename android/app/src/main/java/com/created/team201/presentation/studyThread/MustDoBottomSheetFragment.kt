package com.created.team201.presentation.studyThread

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentMustDoBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.studyThread.model.WarningType

class MustDoBottomSheetFragment :
    BindingBottomSheetFragment<FragmentMustDoBottomSheetBinding>(
        R.layout.fragment_must_do_bottom_sheet,
    ) {
    private val threadViewModel: ThreadViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        setupDayOnClickListener()
        setupPreviousRoundClickListener()
        setupNextRoundClickListener()
        setupMustDoEditClickListener()
        setupMustDoEditDoneClickListener()
        setupMainButtonClickListener()
    }

    private fun setupMainButtonClickListener() {
        binding.tvViewThreadMustDoBottomSheetFragmentButtonMain.setOnClickListener {
            val isMaster: Boolean = threadViewModel.studyDetail.isMaster
            WarningDialog(
                context = requireContext(),
                warningType = WarningType.of(isMaster),
                onAcceptClick = if (isMaster) ::endStudy else ::quitStudy,
            ).show()
        }
    }

    private fun setupMustDoEditDoneClickListener() {
        binding.ivThreadMustDoBottomSheetFragmentMustDoEditDone.setOnClickListener {
            threadViewModel.updateMustDo(binding.etThreadMustDoBottomSheetFragmentMustDoContent.text.toString())
            binding.etThreadMustDoBottomSheetFragmentMustDoContent.isEnabled = false
            it.visibility = View.GONE
        }
    }

    private fun setupMustDoEditClickListener() {
        binding.ivThreadMustDoBottomSheetFragmentMustDoEdit.setOnClickListener {
            binding.etThreadMustDoBottomSheetFragmentMustDoContent.isEnabled =
                !binding.etThreadMustDoBottomSheetFragmentMustDoContent.isEnabled
            binding.ivThreadMustDoBottomSheetFragmentMustDoEditDone.visibility = View.VISIBLE
            binding.etThreadMustDoBottomSheetFragmentMustDoContent.requestFocus()
            val imm: InputMethodManager =
                requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(
                binding.etThreadMustDoBottomSheetFragmentMustDoContent.findFocus(),
                InputMethodManager.SHOW_IMPLICIT,
            )
            binding.etThreadMustDoBottomSheetFragmentMustDoContent.setSelection(binding.etThreadMustDoBottomSheetFragmentMustDoContent.text.length)
        }
    }

    private fun setupNextRoundClickListener() {
        binding.ivThreadMustDoBottomSheetFragmentNextIcon.setOnClickListener {
            threadViewModel.updateNextRound()
        }
    }

    private fun setupPreviousRoundClickListener() {
        binding.ivThreadMustDoBottomSheetFragmentPreviousIcon.setOnClickListener {
            threadViewModel.updatePreviousRound()
        }
    }

    private fun initBinding() {
        binding.viewModel = threadViewModel
        binding.onCancelClickListener = { dismissNow() }
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupDayOnClickListener() {
        binding.dowsThreadMustDoBottomSheetFragmentDayOfWeekSelector.setDayOnClickListener {
            threadViewModel.updateMustDoContent(it)
        }
    }

    private fun endStudy() {
        threadViewModel.endStudy(notifyCantEndStudy())
        dismissNow()
        activity?.finish()
    }

    private fun notifyCantEndStudy(): () -> Unit = {
        Toast.makeText(requireContext(), "스터디 최소 진행기간이 지나지 않았습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun quitStudy() {
        Toast.makeText(requireContext(), "현재 준비중인 기능입니다.", Toast.LENGTH_SHORT).show()
//        threadViewModel.quitStudy()
//        dismissNow()
//        activity?.finish()
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

        fun newInstance(studyId: Long): MustDoBottomSheetFragment =
            MustDoBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY_STUDY_ID, studyId)
                }
            }
    }
}
