package com.created.team201.presentation.myPage

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.setMargins
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentMyPageBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.myPage.MyPageViewModel.State.FAIL
import com.created.team201.presentation.myPage.MyPageViewModel.State.IDLE
import com.created.team201.presentation.myPage.MyPageViewModel.State.SUCCESS
import com.created.team201.presentation.myPage.model.ProfileType
import com.created.team201.presentation.myPage.model.TierProgress
import com.created.team201.presentation.setting.SettingActivity
import com.created.team201.presentation.studyDetail.model.Tier
import com.created.team201.util.FirebaseLogUtil
import com.created.team201.util.FirebaseLogUtil.SCREEN_MY_PAGE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden) {
            myPageViewModel.resetModifyProfile()
            myPageViewModel.setProfileType(ProfileType.VIEW)
        }
    }

    override fun onResume() {
        super.onResume()

        FirebaseLogUtil.logScreenEvent(SCREEN_MY_PAGE, this@MyPageFragment.javaClass.simpleName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        setActionBar()
        initMyProfile()
        setNicknameValidate()
        setMyPageObserve()
        setOnProfileModifyClick()
        observeProfile()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = myPageViewModel
    }

    private fun setActionBar() {
        binding.tbMyPage.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_my_page_setting -> {
                    navigateToSetting()
                    true
                }

                else -> false
            }
        }
    }

    private fun navigateToSetting() {
        startActivity(SettingActivity.getIntent(requireContext()))
    }

    private fun initMyProfile() {
        myPageViewModel.loadProfile()
        myPageViewModel.setProfileType(ProfileType.VIEW)
    }

    private fun setNicknameValidate() {
        binding.etMyPageProfileNickname.setOnFocusChangeListener { _, focus ->
            if (focus) return@setOnFocusChangeListener
            myPageViewModel.checkAvailableNickname()
        }
    }

    private fun setMyPageObserve() {
        myPageViewModel.profileType.observe(viewLifecycleOwner) { profileType ->
            when (profileType) {
                ProfileType.VIEW -> setProfileView(false)
                ProfileType.MODIFY -> setProfileView(true)
                else -> Unit
            }
        }
        myPageViewModel.modifyProfileState.observe(viewLifecycleOwner) { modifyProfileState ->
            when (modifyProfileState) {
                SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.myPage_toast_modify_profile_success),
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                FAIL -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.myPage_toast_modify_profile_failed),
                        Toast.LENGTH_SHORT,
                    ).show()
                    myPageViewModel.resetModifyProfile()
                }

                IDLE -> throw IllegalStateException()
            }
        }
    }

    private fun setOnProfileModifyClick() {
        binding.tvMyPageBtnModifyProfile.setOnClickListener {
            when (myPageViewModel.profileType.value) {
                ProfileType.VIEW -> {
                    myPageViewModel.switchProfileType()
                }

                ProfileType.MODIFY -> {
                    removeDialog()
                    showDialog(
                        getString(R.string.myPage_dialog_modify_profile_title),
                        getString(R.string.myPage_dialog_modify_profile_content),
                        onModifySaveClick(),
                    )
                }

                else -> throw IllegalStateException()
            }
        }
    }

    private fun onModifySaveClick(): MyPageDialogClickListener =
        object : MyPageDialogClickListener {
            override fun onCancelClick() {
                myPageViewModel.resetModifyProfile()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.myPage_toast_modify_profile_cancel),
                    Toast.LENGTH_SHORT,
                ).show()
                myPageViewModel.switchProfileType()
            }

            override fun onOkClick() {
                myPageViewModel.patchMyProfile()
                myPageViewModel.switchProfileType()
            }
        }

    private fun setProfileView(enabled: Boolean) {
        setIntroductionReadWriteMode(enabled)
        binding.etMyPageProfileNickname.isEnabled = enabled
        binding.etMyPageProfileNickname.requestFocus(View.FOCUS_UP)
        binding.tvMyPageNicknameValidateIntroduction.visibility =
            if (enabled) View.VISIBLE else View.GONE
        binding.tbMyPage.menu.forEach { item ->
            item.isVisible = !enabled
        }
        binding.tvMyPageBtnModifyProfile.text =
            if (enabled) getString(R.string.myPage_button_save_profile) else getString(R.string.myPage_button_modify_profile)
    }

    private fun setIntroductionReadWriteMode(enabled: Boolean) {
        with(binding.etMyPageProfileIntroduction) {
            when (enabled) {
                true -> {
                    linksClickable = enabled.not()
                    isCursorVisible = enabled
                    keyListener = EditText(requireContext()).keyListener
                }

                false -> {
                    linksClickable = enabled.not()
                    isCursorVisible = enabled
                    keyListener = null
                }
            }
        }
    }

    private fun showDialog(
        title: String,
        content: String,
        myPageDialogClickListener: MyPageDialogClickListener,
    ) {
        MyPageDialog(title, content, myPageDialogClickListener).show(
            childFragmentManager,
            TAG_DIALOG_MODIFY_PROFILE,
        )
    }

    private fun removeDialog() {
        childFragmentManager.findFragmentByTag(TAG_DIALOG_MODIFY_PROFILE)?.let {
            childFragmentManager.commit {
                remove(it)
            }
        }
    }

    private fun observeProfile() {
        myPageViewModel.profile.observe(viewLifecycleOwner) { profile ->
            val tierProgress =
                TierProgress.of(Tier.of(profile.tier), profile.tierProgress).getTierProgressTable()
            setupTierProgress(tierProgress)
        }
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
        private const val TAG_DIALOG_MODIFY_PROFILE = "TAG_DIALOG_MODIFY_PROFILE"
        private const val TOTAL_BLOCK_COUNT = 20
        private const val DEFAULT_BLOCK_MARGIN = 2
        private const val DEFAULT_DENOMINATOR = 18
    }
}
