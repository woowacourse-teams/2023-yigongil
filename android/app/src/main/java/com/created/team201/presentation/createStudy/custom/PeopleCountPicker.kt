package com.created.team201.presentation.createStudy.custom

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.created.team201.databinding.PeopleCountPickerBinding

class PeopleCountPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleArr) {
    private val binding: PeopleCountPickerBinding by lazy {
        PeopleCountPickerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    val value: Int
        get() = binding.npCreateStudy.value

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.npCreateStudy.textColor = Color.WHITE
        }
    }

    fun setMinValue(min: Int) {
        binding.npCreateStudy.minValue = min
    }

    fun setMaxValue(max: Int) {
        binding.npCreateStudy.maxValue = max
    }

    fun setChangeListener(onChangeListener: PickerChangeListener) {
        binding.npCreateStudy.setOnClickListener {
            onChangeListener.onChange(value)
        }
        binding.npCreateStudy.setOnValueChangedListener { _, _, newValue ->
            onChangeListener.onChange(newValue)
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("minValue")
        fun setMinValue(picker: PeopleCountPicker, min: Int?) {
            min?.let { picker.setMinValue(it) }
        }

        @JvmStatic
        @BindingAdapter("maxValue")
        fun setMaxValue(picker: PeopleCountPicker, max: Int?) {
            max?.let { picker.setMaxValue(it) }
        }

        @JvmStatic
        @BindingAdapter("changeListener")
        fun setChangeListener(picker: PeopleCountPicker, onChangeListener: PickerChangeListener?) {
            onChangeListener?.let { picker.setChangeListener(it) }
        }
    }
}
