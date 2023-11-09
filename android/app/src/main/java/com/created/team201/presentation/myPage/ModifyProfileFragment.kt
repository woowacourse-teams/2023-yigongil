package com.created.team201.presentation.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.created.team201.R
import com.created.team201.databinding.FragmentModifyProfileBinding
import com.created.team201.presentation.myPage.MyPageViewModel.Event.MODIFY_PROFILE
import com.created.team201.presentation.myPage.MyPageViewModel.Event.SAVE_FAILURE
import com.created.team201.presentation.myPage.MyPageViewModel.Event.SAVE_SUCCESS
import com.created.team201.util.collectOnStarted

class ModifyProfileFragment : DialogFragment() {
    private val myPageViewModel: MyPageViewModel by activityViewModels()

    private var _binding: FragmentModifyProfileBinding? = null
    private val binding: FragmentModifyProfileBinding get() = _binding!!

    override fun getTheme(): Int = R.style.NoMarginDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogView()
        setupActionBar()
        setupEditTextChangeListener()
        collectMyProfile()
        collectMyPageEvent()
        collectNicknameState()
    }

    private fun setupDialogView() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    private fun setupActionBar() {
        binding.tbModifyMyPage.setNavigationOnClickListener {
            dismiss()
        }

        binding.tbModifyMyPage.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_modify_profile_save -> {
                    myPageViewModel.changeMyPageEvent(MODIFY_PROFILE)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupEditTextChangeListener() {
        binding.etModifyProfileNickname.filters = myPageViewModel.getInputFilter()
        binding.etModifyProfileNickname.doOnTextChanged { text, _, _, _ ->
            myPageViewModel.setNickname(text.toString())
        }
        binding.etModifyProfileIntroduction.doOnTextChanged { text, _, _, _ ->
            myPageViewModel.setIntroduction(text.toString())
        }
    }

    private fun collectMyProfile() {
        myPageViewModel.profile.collectOnStarted(viewLifecycleOwner) { profile ->
            binding.ivModifyProfile.loadImageUrl(profile.profileImageUrl)
            binding.tvModifyProfileId.text = profile.githubId
            binding.etModifyProfileNickname.setText(profile.profileInformation.nickname.nickname)
            binding.etModifyProfileIntroduction.setText(profile.profileInformation.introduction)
        }
    }

    private fun collectNicknameState() {
        myPageViewModel.nicknameState.collectOnStarted(viewLifecycleOwner) { state ->
            with(binding.tvModifyMyPageNicknameValidateIntroduction) {
                text = getString(state.introduction)
                setTextColor(ContextCompat.getColor(requireContext(), state.color))
            }
        }
    }

    private fun collectMyPageEvent() {
        myPageViewModel.myPageEvent.collectOnStarted(viewLifecycleOwner) { event ->
            when (event) {
                SAVE_SUCCESS -> showToast(getString(R.string.myPage_toast_modify_profile_success))
                SAVE_FAILURE -> showToast(getString(R.string.myPage_toast_modify_profile_failed))
                MODIFY_PROFILE -> myPageViewModel.patchMyProfile()
                else -> Unit
            }
        }
    }

    private fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    private fun ImageView.loadImageUrl(imageUrl: String?) {
        Glide.with(requireContext())
            .load(imageUrl)
            .into(this)
    }

    companion object {
        const val TAG_MODIFY_FRAGMENT = "TAG_MODIFY_FRAGMENT"
    }
}
