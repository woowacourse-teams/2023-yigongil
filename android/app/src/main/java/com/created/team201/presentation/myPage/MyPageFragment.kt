package com.created.team201.presentation.myPage

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.setMargins
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.created.team201.R
import com.created.team201.databinding.FragmentMyPageBinding
import com.created.team201.presentation.myPage.MyPageViewModel.Event.EnableModify
import com.created.team201.presentation.myPage.MyPageViewModel.Event.ModifyMyPage
import com.created.team201.presentation.myPage.MyPageViewModel.Event.ShowDialog
import com.created.team201.presentation.myPage.MyPageViewModel.Event.ShowToast
import com.created.team201.presentation.myPage.MyPageViewModel.State.Fail
import com.created.team201.presentation.myPage.MyPageViewModel.State.Loading
import com.created.team201.presentation.myPage.MyPageViewModel.State.Success
import com.created.team201.presentation.myPage.model.ProfileType
import com.created.team201.presentation.myPage.model.TierProgress
import com.created.team201.presentation.setting.SettingActivity
import com.created.team201.presentation.studyDetail.model.Tier
import com.created.team201.util.FirebaseLogUtil
import com.created.team201.util.FirebaseLogUtil.SCREEN_MY_PAGE
import com.created.team201.util.collectOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private val myPageViewModel: MyPageViewModel by viewModels()
    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMyProfile()
        setActionBar()
        setOnProfileModifyClick()
        setEditTextChangeListener()
        collectMyProfileType()
        collectModifyProfileState()
        collectNicknameState()
        collectMyProfile()
        collectMyPageEvent()
    }

    private fun initMyProfile() {
        myPageViewModel.loadProfile()
        myPageViewModel.setProfileType(ProfileType.VIEW)
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

    private fun setOnProfileModifyClick() {
        binding.tvMyPageBtnModifyProfile.setOnClickListener {
            when (myPageViewModel.profileType.value) {
                ProfileType.VIEW -> {
                    myPageViewModel.switchProfileType()
                }

                ProfileType.MODIFY -> {
                    myPageViewModel.changeMyPageEvent(ShowDialog)
                }
            }
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

    private fun setEditTextChangeListener() {
        binding.etMyPageProfileNickname.filters = myPageViewModel.getInputFilter()
        binding.etMyPageProfileNickname.doOnTextChanged { text, _, _, _ ->
            myPageViewModel.setNickname(text.toString())
        }
        binding.etMyPageProfileIntroduction.doOnTextChanged { text, _, _, _ ->
            myPageViewModel.setIntroduction(text.toString())
        }
    }

    private fun collectMyProfileType() {
        myPageViewModel.profileType.collectOnStarted(viewLifecycleOwner) { profileType ->
            when (profileType) {
                ProfileType.VIEW -> setProfileView(false)
                ProfileType.MODIFY -> setProfileView(true)
            }
        }
    }

    private fun collectModifyProfileState() {

        myPageViewModel.modifyProfileState.collectOnStarted(viewLifecycleOwner) { modifyProfileState ->
            when (modifyProfileState) {
                Loading -> Unit
                Success -> {
                    myPageViewModel.changeMyPageEvent(
                        ShowToast(getString(R.string.myPage_toast_modify_profile_success))
                    )
                }

                Fail -> {
                    myPageViewModel.changeMyPageEvent(
                        ShowToast(getString(R.string.myPage_toast_modify_profile_failed))
                    )
                }
            }
        }
    }

    private fun collectNicknameState() {
        myPageViewModel.nicknameState.collectOnStarted(viewLifecycleOwner) { state ->
            with(binding.tvMyPageNicknameValidateIntroduction) {
                text = getString(state.introduction)
                setTextColor(requireContext().getColor(state.color))
            }
        }
    }

    private fun collectMyProfile() {
        myPageViewModel.profile.collectOnStarted(viewLifecycleOwner) { profile ->
            binding.ivMyPageProfile.loadImageUrl(profile.profileImageUrl)
            binding.tvMyPageProfileId.text = profile.githubId
            binding.etMyPageProfileNickname.setText(profile.profileInformation.nickname.nickname)
            binding.etMyPageProfileIntroduction.setText(profile.profileInformation.introduction)
            binding.layoutMyPageStudySuccessRate.result =
                getString(R.string.profile_success_rate_format, profile.successRate)
            binding.layoutMyPageTodoSuccessRate.result =
                getString(R.string.profile_todo_success_rate_format, profile.successfulRoundCount)
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
                is ShowToast -> showToast(event.message)
                ShowDialog -> {
                    removeDialog()
                    showDialog(
                        getString(R.string.myPage_dialog_modify_profile_title),
                        getString(R.string.myPage_dialog_modify_profile_content),
                        onModifySaveClick(),
                    )
                }

                ModifyMyPage -> myPageViewModel.patchMyProfile()
                is EnableModify -> binding.tvMyPageBtnModifyProfile.isEnabled = event.isEnable
            }
        }
    }

    private fun removeDialog() {
        childFragmentManager.findFragmentByTag(TAG_DIALOG_MODIFY_PROFILE)?.let {
            childFragmentManager.commit {
                remove(it)
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

    private fun onModifySaveClick(): MyPageDialogClickListener =
        object : MyPageDialogClickListener {
            override fun onCancelClick() {
                myPageViewModel.changeMyPageEvent(
                    ShowToast(getString(R.string.myPage_toast_modify_profile_cancel))
                )
                myPageViewModel.resetModifyProfile()
                myPageViewModel.switchProfileType()
            }

            override fun onOkClick() {
                myPageViewModel.changeMyPageEvent(ModifyMyPage)
                myPageViewModel.switchProfileType()
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

    private fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    companion object {
        private const val TAG_DIALOG_MODIFY_PROFILE = "TAG_DIALOG_MODIFY_PROFILE"
        private const val TOTAL_BLOCK_COUNT = 20
        private const val DEFAULT_BLOCK_MARGIN = 2
        private const val DEFAULT_DENOMINATOR = 18
    }
}
