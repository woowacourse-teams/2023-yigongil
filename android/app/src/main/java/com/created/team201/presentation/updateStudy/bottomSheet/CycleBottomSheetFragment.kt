package com.created.team201.presentation.updateStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentCycleBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.updateStudy.UpdateStudyViewModel
import com.created.team201.presentation.studyDetail.custom.MultiPickerChangeListener

class CycleBottomSheetFragment :
    BindingBottomSheetFragment<FragmentCycleBottomSheetBinding>(R.layout.fragment_cycle_bottom_sheet) {
    private val viewModel: UpdateStudyViewModel by activityViewModels()

    private val numbers: IntArray by lazy {
        resources.getIntArray(R.array.multiPickerMaxNumbers)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        setChangeListener()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.onCancelClickListener = { dismiss() }
        binding.onSaveClickListener = ::onSaveButtonClick
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setChangeListener() {
        binding.changeListener = object : MultiPickerChangeListener {
            override fun onLeftChange(value: Int) {
            }

            override fun onRightChange(value: Int) {
                binding.mpCycle.setLeftMaxValue(numbers[value])
            }
        }
    }

    private fun onSaveButtonClick() {
        viewModel.setCycle(binding.mpCycle.leftValue, binding.mpCycle.rightValue)
        dismiss()
    }
}
