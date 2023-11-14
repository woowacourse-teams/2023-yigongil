package com.created.team201.presentation.studyDetail

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.created.team201.R
import com.created.team201.databinding.CustomProfileImageBinding
import com.created.team201.presentation.studyDetail.model.Tier

class CustomProfileImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private val binding: CustomProfileImageBinding by lazy {
        CustomProfileImageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val borderProgress: ProgressBar by lazy { binding.pbCustomProfileImageSuccessRate }
    private val profileImage: ImageView by lazy { binding.ivCustomProfileImage }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomProfileImage, 0, 0).apply {
            runCatching {
                borderProgress.progress =
                    getInteger(R.styleable.CustomProfileImage_borderProgress, 0)
                borderProgress.progressTintList =
                    ColorStateList.valueOf(
                        context.getColor(
                            Tier.of(
                                getInteger(
                                    R.styleable.CustomProfileImage_tier,
                                    0,
                                ),
                            ).color,
                        ),
                    )
                Glide.with(profileImage.context)
                    .load(getString(R.styleable.CustomProfileImage_glideCircleImageUrl))
                    .circleCrop()
                    .into(profileImage)
            }.also { recycle() }
        }
    }

    fun setGlideCircleImageUrl(image: String) {
        Glide.with(profileImage.context)
            .load(image)
            .circleCrop()
            .into(profileImage)
    }

    fun setBorderProgress(successRate: Int) {
        borderProgress.progress = successRate
    }

    fun setTier(tierName: String) {
        val tier = Tier.of(tierName)
        borderProgress.progressTintList = ColorStateList.valueOf(context.getColor(tier.color))
    }

    fun setTier(tierNumber: Int) {
        val tier = Tier.of(tierNumber)
        borderProgress.progressTintList = ColorStateList.valueOf(context.getColor(tier.color))
    }
}
