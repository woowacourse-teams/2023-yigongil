package com.created.team201.presentation.createStudy.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.created.team201.databinding.IconTextButtonBinding

class IconTextButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleArr) {
    private val binding: IconTextButtonBinding by lazy {
        IconTextButtonBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setIcon(icon: Drawable) {
        binding.ivIconTextButton.setImageDrawable(icon)
    }

    fun setName(name: String) {
        binding.tvIconTextButtonName.text = name
    }

    fun setInformation(information: String) {
        binding.llIconTextButton.background = null
        binding.tvIconTextButtonInformation.visibility = View.VISIBLE
        binding.tvIconTextButtonInformation.text = information
    }
}
