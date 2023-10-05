package com.created.team201.presentation.createStudy.util

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:textIntValue", "app:textFormatter")
fun TextView.setTextValue(value: Int, formatter: String) {
    if (value == -1 || formatter.isEmpty()) return
    text = formatter.format(value)
}
