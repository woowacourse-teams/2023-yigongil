package com.created.team201.presentation.myPage.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.created.team201.R
import com.created.team201.databinding.CustomUserStudyResultBinding

class UserStudyResult @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleArr) {
    private val binding: CustomUserStudyResultBinding by lazy {
        CustomUserStudyResultBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var resultResource: String? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.UserStudyResult, 0, 0).apply {
            runCatching {
                with(binding) {
                    tvUserStudyResultInformationTitle.text =
                        getString(R.styleable.UserStudyResult_android_title)

                    resultResource = getString(R.styleable.UserStudyResult_userStudyResultResource)
                    ivUserStudyResultInformation.setImageDrawable(
                        getDrawable(R.styleable.UserStudyResult_userStudyIconResource),
                    )
                }

            }.also {
                recycle()
            }
        }
    }

    fun setResult(result: String) {
        binding.tvUserStudyResultInformationResult.text =
            resultResource?.format(result) ?: result
    }
}
