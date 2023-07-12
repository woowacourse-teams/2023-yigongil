package com.created.team201.presentation.createStudy.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.created.team201.databinding.PeopleCountPickerBinding

class PeopleCountPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleArr) {
    private val binding: PeopleCountPickerBinding by lazy {
        PeopleCountPickerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setValue(value: Int) {
        binding.npCreateStudy.value = value
    }

    fun setMinValue(min: Int) {
        binding.npCreateStudy.minValue = min
    }

    fun setMaxValue(max: Int) {
        binding.npCreateStudy.maxValue = max
    }

    fun setChangeListener(onChangeListener: PickerChangeListener) {
        binding.npCreateStudy.setOnValueChangedListener { _, _, newValue ->
            onChangeListener.onChange(newValue)
        }
    }
}
