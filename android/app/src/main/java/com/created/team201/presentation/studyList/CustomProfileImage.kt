package com.created.team201.presentation.studyList

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.created.team201.databinding.CustomProfileImageBinding
import com.created.team201.presentation.studyList.uiModel.Tier

class CustomProfileImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: CustomProfileImageBinding by lazy {
        CustomProfileImageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val studySuccessRate: ProgressBar by lazy { binding.pbCustomProfileImageSuccessRate }
    private val profileImage: ImageView by lazy { binding.ivCustomProfileImage }

    fun setGlideCircleImageUrl(image: String) {
        Glide.with(profileImage.context)
            .load(image)
            .circleCrop()
            .into(profileImage)
    }

    fun setSuccessRate(successRate: Int) {
        studySuccessRate.progress = successRate
    }

    fun setTier(tierName: String) {
        val tier = Tier.of(tierName)
        studySuccessRate.progressTintList = ColorStateList.valueOf(context.getColor(tier.color))
    }
}
