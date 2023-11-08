package com.created.team201.presentation.myPage

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.view.setMargins
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.created.team201.R
import com.created.team201.databinding.FragmentMyPageBinding
import com.created.team201.presentation.common.BindingViewFragment
import com.created.team201.presentation.myPage.MyPageViewModel.Event.NavigateToModify
import com.created.team201.presentation.myPage.MyPageViewModel.Event.NavigateToSetting
import com.created.team201.presentation.myPage.model.TierProgress
import com.created.team201.presentation.setting.SettingActivity
import com.created.team201.presentation.studyDetail.model.Tier
import com.created.team201.util.FirebaseLogUtil
import com.created.team201.util.FirebaseLogUtil.SCREEN_MY_PAGE
import com.created.team201.util.collectOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BindingViewFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()

        FirebaseLogUtil.logScreenEvent(SCREEN_MY_PAGE, this@MyPageFragment.javaClass.simpleName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMyProfile()
        setupActionBar()
        setupOnProfileModifyClick()
        collectMyProfile()
        collectMyPageEvent()
    }

    private fun initMyProfile() = myPageViewModel.loadProfile()

    private fun setupActionBar() {
        binding.tbMyPage.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_my_page_setting -> {
                    myPageViewModel.setMyPageEvent(NavigateToSetting)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupOnProfileModifyClick() {
        binding.tvMyPageBtnModifyProfile.setOnClickListener {
            myPageViewModel.setMyPageEvent(NavigateToModify)
        }
    }

    private fun collectMyProfile() {
        myPageViewModel.profile.collectOnStarted(viewLifecycleOwner) { profile ->
            binding.ivMyPageProfile.loadImageUrl(profile.profileImageUrl)
            binding.tvMyPageProfileId.text = profile.githubId
            binding.tvMyPageProfileNickname.text = profile.profileInformation.nickname.nickname
            binding.tvMyPageProfileIntroduction.text = profile.profileInformation.introduction
            binding.userStudyResultMyPageStudySuccessRate.setResult(profile.successRate.toString())
            binding.userStudyResultMyPageMustdoSuccessRate.setResult(profile.successfulRoundCount.toString())
            setupTierProgress(
                TierProgress.of(
                    Tier.of(profile.tier),
                    profile.tierProgress,
                ).getTierProgressTable(),
            )
        }
    }

    private fun ImageView.loadImageUrl(imageUrl: String?) {
        Glide.with(requireContext())
            .load(imageUrl)
            .into(this)
    }

    private fun collectMyPageEvent() {
        myPageViewModel.myPageEvent.collectOnStarted(viewLifecycleOwner) { event ->
            when (event) {
                NavigateToModify -> navigateToModify()
                NavigateToSetting -> navigateToSetting()
                else -> Unit
            }
        }
    }

    private fun navigateToModify() {
        startActivity(
            ModifyProfileActivity.getIntent(requireContext()).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun navigateToSetting() {
        startActivity(
            SettingActivity.getIntent(requireContext()).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun setupTierProgress(tierProgress: List<Int>) {
        binding.glMyPageTierProgress.removeAllViews()
        repeat(TOTAL_BLOCK_COUNT) {
            val params: GridLayout.LayoutParams = getDefaultTierProgressBlockParams()

            binding.glMyPageTierProgress.addView(
                ImageView(requireContext()).apply {
                    setImageResource(tierProgress[it])
                    scaleType = ImageView.ScaleType.FIT_XY
                    layoutParams = params
                },
            )
        }
    }

    private fun getDefaultTierProgressBlockParams(): GridLayout.LayoutParams =
        GridLayout.LayoutParams().apply {
            width = resources.displayMetrics.widthPixels / DEFAULT_DENOMINATOR
            height = width
            setMargins(getDpToPixel(DEFAULT_BLOCK_MARGIN))
        }

    private fun getDpToPixel(dp: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        requireContext().resources.displayMetrics,
    ).toInt()

    companion object {
        private const val TOTAL_BLOCK_COUNT = 20
        private const val DEFAULT_BLOCK_MARGIN = 2
        private const val DEFAULT_DENOMINATOR = 18
    }
}
