package com.created.team201.presentation.createStudy.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.created.domain.model.Period.Companion.TYPE_DAY
import com.created.domain.model.Period.Companion.TYPE_WEEK
import com.created.team201.R
import com.created.team201.databinding.FragmentPeriodBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.createStudy.CreateStudyViewModel
import com.created.team201.presentation.createStudy.custom.MultiPickerChangeListener
import com.created.team201.presentation.createStudy.model.PeriodUiModel

class PeriodBottomSheetFragment : BindingBottomSheetFragment<FragmentPeriodBottomSheetBinding>(
    R.layout.fragment_period_bottom_sheet,
) {
    private val viewModel: CreateStudyViewModel by activityViewModels()

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
        binding.bottomSheetFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
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

    fun onSaveButtonClick() {
        viewModel.period.value =
            PeriodUiModel(binding.mpPeriod.leftValue, binding.mpPeriod.rightValue)
        viewModel.cycleMaxDates.value = arrayOf(
            viewModel.getCycleDateMaxValue(TYPE_DAY),
            viewModel.getCycleDateMaxValue(TYPE_WEEK),
        )
        dismiss()
    }
}
