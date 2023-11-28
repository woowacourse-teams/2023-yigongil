package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.created.team201.databinding.FragmentCycleBottomSheetBinding
import com.created.team201.presentation.common.BindingViewBottomSheetFragment
import com.created.team201.presentation.createStudy.CreateStudyViewModel
import com.created.team201.util.collectOnStarted
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class CycleBottomSheetFragment :
    BindingViewBottomSheetFragment<FragmentCycleBottomSheetBinding>(
        FragmentCycleBottomSheetBinding::inflate,
    ) {
    private val createStudyViewModel: CreateStudyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.let {
            val behavior = (it as BottomSheetDialog).behavior
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            it.setCanceledOnTouchOutside(false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSaveButtonClick()
        setupCancelButtonClick()
        collectCycle()
    }

    private fun setupSaveButtonClick() {
        binding.tvCycleBottomSheetBtnSave.setOnClickListener {
            createStudyViewModel.setCycle(binding.spCycle.value)
            dismiss()
        }
    }

    private fun setupCancelButtonClick() {
        binding.tvCycleBottomSheetBtnCancel.setOnClickListener { dismiss() }
    }

    private fun collectCycle() {
        createStudyViewModel.cycle.collectOnStarted(viewLifecycleOwner) { cycle ->
            binding.spCycle.setValue(cycle)
        }
    }
}
