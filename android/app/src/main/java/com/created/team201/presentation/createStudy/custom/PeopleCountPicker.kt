package com.created.team201.presentation.createStudy.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.created.team201.R
import com.created.team201.databinding.PeopleCountPickerBinding

class PeopleCountPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleArr) {
    private val binding: PeopleCountPickerBinding by lazy {
        PeopleCountPickerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.PeopleCountPicker, 0, 0).apply {
            runCatching {
                binding.npCreateStudy.value =
                    getInteger(R.styleable.PeopleCountPicker_value, DEFAULT_MIN_VALUE)
                binding.npCreateStudy.minValue =
                    getInteger(R.styleable.PeopleCountPicker_minValue, DEFAULT_MIN_VALUE)
                binding.npCreateStudy.maxValue =
                    getInteger(R.styleable.PeopleCountPicker_minValue, DEFAULT_MAX_VALUE)
            }.also {
                recycle()
            }
        }
    }

    fun setValue(value: Int) {
        if (value == 0) return
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

    companion object {
        private const val DEFAULT_MIN_VALUE = 2
        private const val DEFAULT_MAX_VALUE = 8
    }
}
