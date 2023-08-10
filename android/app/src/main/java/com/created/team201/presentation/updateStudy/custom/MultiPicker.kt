package com.created.team201.presentation.updateStudy.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.created.team201.R
import com.created.team201.databinding.MultiPickerBinding

class MultiPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleArr) {
    private val binding: MultiPickerBinding by lazy {
        MultiPickerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    val leftValue: Int
        get() = binding.npMultiPickerLeft.value

    val rightValue: Int
        get() = binding.npMultiPickerRight.value

    init {
        binding
        context.theme.obtainStyledAttributes(attrs, R.styleable.MultiPicker, 0, 0).apply {
            runCatching {
                binding.npMultiPickerLeft.value =
                    getInteger(R.styleable.MultiPicker_leftValue, DEFAULT_VALUE)
                binding.npMultiPickerRight.value =
                    getInteger(R.styleable.MultiPicker_rightValue, DEFAULT_VALUE)
                binding.npMultiPickerLeft.minValue =
                    getInteger(R.styleable.MultiPicker_leftMinValue, DEFAULT_LEFT_MIN_VALUE)
                binding.npMultiPickerRight.minValue =
                    getInteger(R.styleable.MultiPicker_rightMinValue, DEFAULT_LEFT_MIN_VALUE)
                binding.npMultiPickerLeft.maxValue =
                    getInteger(R.styleable.MultiPicker_leftMaxValue, DEFAULT_LEFT_MAX_VALUE)
                binding.npMultiPickerRight.maxValue =
                    getInteger(R.styleable.MultiPicker_rightMaxValue, DEFAULT_LEFT_MAX_VALUE)
                binding.npMultiPickerRight.displayedValues =
                    getTextArray(R.styleable.MultiPicker_rightDisplayNames).map {
                        it.toString()
                    }.toTypedArray()
            }.also {
                recycle()
            }
        }
    }

    fun setLeftValue(leftValue: Int) {
        if (leftValue == 0) return
        binding.npMultiPickerLeft.value = leftValue
    }

    fun setRightValue(rightValue: Int) {
        if (rightValue == 0) return
        binding.npMultiPickerRight.value = rightValue
    }

    fun setLeftMaxValue(max: Int) {
        binding.npMultiPickerLeft.maxValue = max
    }

    fun setChangeListener(
        leftOnChangeListener: (Int) -> Unit,
        rightOnChangeListener: (Int) -> Unit,
    ) {
        binding.npMultiPickerLeft.setOnValueChangedListener { _, _, newVal ->
            leftOnChangeListener(newVal)
        }
        binding.npMultiPickerRight.setOnValueChangedListener { _, _, newVal ->
            rightOnChangeListener(newVal)
        }
    }

    companion object {
        private const val DEFAULT_VALUE = -1
        private const val DEFAULT_LEFT_MIN_VALUE = -1
        private const val DEFAULT_LEFT_MAX_VALUE = -1
    }
}
