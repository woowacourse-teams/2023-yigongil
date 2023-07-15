package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentCycleBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.createStudy.CreateStudyViewModel
import com.created.team201.presentation.createStudy.custom.MultiPickerChangeListener
import com.created.team201.presentation.createStudy.model.PeriodUiModel

class CycleBottomSheetFragment :
    BindingBottomSheetFragment<FragmentCycleBottomSheetBinding>(R.layout.fragment_cycle_bottom_sheet) {
    private val viewModel: CreateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.bottomSheetFragment = this
        initMultiPicker()
        setChangeListener()
    }

    private fun initMultiPicker() {
        binding.mpCycle.setLeftMaxValue(viewModel.getCycleDateMaxValue(binding.mpCycle.rightValue))
        binding.mpCycle.setRightMaxValue(viewModel.getCycleTypeMaxValue())
    }

    private fun setChangeListener() {
        binding.changeListener = object : MultiPickerChangeListener {
            override fun onLeftChange(value: Int) {
            }

            override fun onRightChange(value: Int) {
                viewModel.cycleMaxDates.value?.let {
                    binding.mpCycle.setLeftMaxValue(it[binding.mpCycle.rightValue])
                }
            }
        }
    }

    fun onSaveButtonClick() {
        viewModel.cycle.value =
            PeriodUiModel(binding.mpCycle.leftValue, binding.mpCycle.rightValue)
        dismiss()
    }
}
