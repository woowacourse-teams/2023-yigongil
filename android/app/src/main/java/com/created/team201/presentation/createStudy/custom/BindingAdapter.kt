package com.created.team201.presentation.createStudy.custom

import androidx.databinding.BindingAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("changeListener")
    fun PeopleCountPicker.setChangeListener(onChangeListener: PickerChangeListener?) {
        onChangeListener?.let { setChangeListener(it) }
    }

    @JvmStatic
    @BindingAdapter("changeListener")
    fun Calendar.setOnDateChangedListener(onChangeListener: CalendarChangeListener?) {
        onChangeListener?.let { setOnDateChangedListener(it) }
    }

    @JvmStatic
    @BindingAdapter("changeListener")
    fun MultiPicker.setChangeListener(onChangeListener: MultiPickerChangeListener?) {
        onChangeListener?.let { setChangeListener(it::onLeftChange, it::onRightChange) }
    }
}
