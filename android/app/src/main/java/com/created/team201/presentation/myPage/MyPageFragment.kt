package com.created.team201.presentation.myPage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentMyPageBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.myPage.MyPageViewModel.State.FAIL
import com.created.team201.presentation.myPage.MyPageViewModel.State.IDLE
import com.created.team201.presentation.myPage.MyPageViewModel.State.SUCCESS
import com.created.team201.presentation.myPage.model.ProfileType
import com.created.team201.presentation.setting.SettingActivity

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val myPageViewModel: MyPageViewModel by viewModels {
        MyPageViewModel.Factory
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden) {
            myPageViewModel.loadProfile()
            myPageViewModel.setProfileType(ProfileType.VIEW)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        setActionBar()
        initMyProfile()
        setNicknameValidate()
        setObserveProfileType()
        setOnProfileModifyClick()
        setObserveModifyProfile()
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
            myPageViewModel.getAvailableNickname()
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
                        onModifySaveClick()
                    )
                }

                else -> throw IllegalStateException()
            }
        }
    }

    private fun onModifySaveClick(): MyPageDialogClickListener =
        object : MyPageDialogClickListener {
            override fun onCancelClick() {
                myPageViewModel.loadProfile()
                showToast(getString(R.string.myPage_toast_modify_profile_cancel))
                myPageViewModel.switchProfileType()
            }

            override fun onOkClick() {
                myPageViewModel.patchMyProfile()
                myPageViewModel.switchProfileType()
            }
        }

    private fun setObserveProfileType() {
        myPageViewModel.profileType.observe(viewLifecycleOwner) { profileType ->
            when (profileType) {
                ProfileType.VIEW -> setProfileView(false)
                ProfileType.MODIFY -> setProfileView(true)
            }
        }
    }

    private fun setObserveModifyProfile() {
        myPageViewModel.modifyProfileState.observe(viewLifecycleOwner) { modifyProfileState ->
            when (modifyProfileState) {
                SUCCESS -> {
                    showToast(getString(R.string.myPage_toast_modify_profile_success))
                }

                FAIL -> {
                    showToast(getString(R.string.myPage_toast_modify_profile_failed))
                    myPageViewModel.resetModifyProfile()
                }

                IDLE -> throw IllegalStateException()
            }
            myPageViewModel.loadProfile()
        }
    }

    private fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    private fun setProfileView(enabled: Boolean) {
        binding.etMyPageProfileNickname.isEnabled = enabled
        binding.etMyPageProfileIntroduction.isEnabled = enabled
        binding.etMyPageProfileNickname.requestFocus(View.FOCUS_UP)
        binding.tvMyPageNicknameValidateIntroduction.visibility =
            if (enabled) View.VISIBLE else View.GONE
        binding.tbMyPage.menu.forEach { item ->
            item.isVisible = !enabled
        }
        binding.tvMyPageBtnModifyProfile.text =
            if (enabled) getString(R.string.myPage_button_save_profile) else getString(R.string.myPage_button_modify_profile)
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

    companion object {
        private const val TAG_DIALOG_MODIFY_PROFILE = "TAG_DIALOG_MODIFY_PROFILE"
    }
}
