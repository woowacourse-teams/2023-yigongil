package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentPeriodBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.createStudy.CreateStudyViewModel
import com.created.team201.presentation.createStudy.custom.MultiPickerChangeListener

class PeriodBottomSheetFragment : BindingBottomSheetFragment<FragmentPeriodBottomSheetBinding>(
    R.layout.fragment_period_bottom_sheet,
) {
    private val viewModel: CreateStudyViewModel by activityViewModels()

    private val numbers: IntArray by lazy {
        resources.getIntArray(R.array.multiPickerMaxNumbers)
    }

    private val dates: Array<String> by lazy {
        resources.getStringArray(R.array.multiPickerDisplayNames)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        setChangeListener()
        setButtonListener()
    }

    private fun setChangeListener() {
        binding.changeListener = object : MultiPickerChangeListener {
            override fun onLeftChange(value: Int) {
            }

            override fun onRightChange(value: Int) {
                binding.mpPeriod.setLeftMaxValue(numbers[value])
            }
        }
    }

    private fun setButtonListener() {
        binding.tvPeriodBottomSheetBtnCancel.setOnClickListener { dismiss() }
        binding.tvPeriodBottomSheetBtnSave.setOnClickListener {
            viewModel.period.value =
                getString(
                    R.string.multiPicker_formatter_information,
                    binding.mpPeriod.leftValue,
                    dates[binding.mpPeriod.rightValue],
                )
            dismiss()
        }
    }
}
