package com.created.team201.presentation.createStudy.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.created.team201.R
import com.created.team201.databinding.IconTextButtonBinding

class IconTextButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleArr) {
    private val binding: IconTextButtonBinding by lazy {
        IconTextButtonBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.IconTextButton, 0, 0).apply {
            runCatching {
                binding.tvIconTextButtonName.text =
                    getString(R.styleable.IconTextButton_buttonName)
                binding.tvIconTextButtonInformation.text =
                    getString(R.styleable.IconTextButton_information)
                binding.ivIconTextButton.setImageDrawable(
                    getDrawable(R.styleable.IconTextButton_iconResource),
                )
            }.also {
                recycle()
            }
        }
    }

    fun setInformation(information: String?) {
        if (information.isNullOrEmpty()) return
        binding.llIconTextButton.background = null
        binding.tvIconTextButtonInformation.visibility = View.VISIBLE
        binding.tvIconTextButtonInformation.text = information
    }
}
