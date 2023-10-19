package com.created.team201.presentation.studyDetail.custom

import androidx.databinding.BindingAdapter

object MultiPickerBindingAdapter {
    @JvmStatic
    @BindingAdapter("changeListener")
    fun MultiPicker.setChangeListener(onChangeListener: MultiPickerChangeListener?) {
        onChangeListener?.let { setChangeListener(it::onLeftChange, it::onRightChange) }
    }
}
