package com.created.team201.presentation.createStudy.custom

import androidx.databinding.BindingAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("changeListener")
    fun PeopleCountPicker.setChangeListener(onChangeListener: PickerChangeListener?) {
        onChangeListener?.let { setChangeListener(it) }
    }
}
