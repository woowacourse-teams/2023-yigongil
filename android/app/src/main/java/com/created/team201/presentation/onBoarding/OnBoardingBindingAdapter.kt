package com.created.team201.presentation.onBoarding

import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.created.team201.presentation.onBoarding.model.NicknameState

object OnBoardingBindingAdapter {
    @JvmStatic
    @BindingAdapter("editText:inputFilter")
    fun EditText.setEditTextInputFilter(vararg inputFilters: InputFilter) {
        this.filters = inputFilters
    }

    @JvmStatic
    @BindingAdapter("textView:nicknameState")
    fun TextView.setTextViewColor(nicknameState: NicknameState?) {
        nicknameState?.let {
            this.setTextColor(context.getColor(it.color))
            this.text = context.getString(it.introduction)
        }
    }
}
