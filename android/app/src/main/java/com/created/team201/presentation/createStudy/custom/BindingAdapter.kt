package com.created.team201.presentation.createStudy.custom

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("icon")
    fun IconTextButton.setIcon(icon: Drawable?) {
        icon?.let { setIcon(it) }
    }

    @JvmStatic
    @BindingAdapter("name")
    fun IconTextButton.setName(name: String?) {
        name?.let { setName(it) }
    }

    @JvmStatic
    @BindingAdapter("information")
    fun IconTextButton.setInformation(information: String?) {
        information?.let {
            setInformation(it)
        }
    }

    @JvmStatic
    @BindingAdapter("minValue")
    fun PeopleCountPicker.setMinValue(min: Int?) {
        min?.let { setMinValue(it) }
    }

    @JvmStatic
    @BindingAdapter("maxValue")
    fun PeopleCountPicker.setMaxValue(max: Int?) {
        max?.let { setMaxValue(it) }
    }

    @JvmStatic
    @BindingAdapter("changeListener")
    fun PeopleCountPicker.setChangeListener(onChangeListener: PickerChangeListener?) {
        onChangeListener?.let { setChangeListener(it) }
    }
}
