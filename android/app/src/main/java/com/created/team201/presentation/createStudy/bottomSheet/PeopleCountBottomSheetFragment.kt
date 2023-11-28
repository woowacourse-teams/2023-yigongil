package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.created.team201.databinding.FragmentPeopleCountBottomSheetBinding
import com.created.team201.presentation.common.BindingViewBottomSheetFragment
import com.created.team201.presentation.createStudy.CreateStudyViewModel
import com.created.team201.util.collectOnStarted
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class PeopleCountBottomSheetFragment :
    BindingViewBottomSheetFragment<FragmentPeopleCountBottomSheetBinding>(
        FragmentPeopleCountBottomSheetBinding::inflate,
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
        collectPeopleCount()
    }

    private fun setupSaveButtonClick() {
        binding.tvPeopleCountBottomSheetBtnSave.setOnClickListener {
            createStudyViewModel.setPeopleCount(binding.spPeopleCount.value)
            dismiss()
        }
    }

    private fun setupCancelButtonClick() {
        binding.tvPeopleCountBottomSheetBtnCancel.setOnClickListener { dismiss() }
    }

    private fun collectPeopleCount() {
        createStudyViewModel.peopleCount.collectOnStarted(viewLifecycleOwner) { peopleCount ->
            binding.spPeopleCount.setValue(peopleCount)
        }
    }
}
