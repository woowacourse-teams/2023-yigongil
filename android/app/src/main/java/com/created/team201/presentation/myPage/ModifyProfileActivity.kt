package com.created.team201.presentation.myPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.created.team201.R
import com.created.team201.databinding.ActivityModifyProfileBinding
import com.created.team201.presentation.common.BindingViewActivity
import com.created.team201.util.collectOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModifyProfileActivity :
    BindingViewActivity<ActivityModifyProfileBinding>(ActivityModifyProfileBinding::inflate) {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupActionBar()
        setupEditTextChangeListener()
        collectMyProfile()
        collectMyProfileInformation()
        collectModifyProfileState()
        collectNicknameState()
    }

    private fun collectMyProfile() {
        myPageViewModel.profile.collectOnStarted(this) { profile ->
            binding.ivModifyProfile.loadImageUrl(profile.profileImageUrl)
            binding.tvModifyProfileId.text = profile.githubId
            binding.etModifyProfileNickname.setText(profile.profileInformation.nickname.nickname)
            binding.etModifyProfileIntroduction.setText(profile.profileInformation.introduction)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.tbModifyMyPage)
        supportActionBar?.setHomeActionContentDescription(R.string.toolbar_back_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_modify_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_modify_profile_save -> {
                myPageViewModel.patchMyProfile()
                true
            }

            android.R.id.home -> {
                finish()
                true
            }

            else -> false
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

    private fun collectMyProfileInformation() {
        myPageViewModel.nickname.collectOnStarted(this) { nickname ->
            if (binding.etModifyProfileNickname.text.toString() == nickname) return@collectOnStarted
            binding.etModifyProfileNickname.setText(nickname)
        }

        myPageViewModel.introduction.collectOnStarted(this) { introduction ->
            if (binding.etModifyProfileIntroduction.text.toString() == introduction) return@collectOnStarted
            binding.etModifyProfileIntroduction.setText(introduction)
        }
    }

    private fun collectModifyProfileState() {
        myPageViewModel.modifyProfileState.collectOnStarted(this) { modifyProfileState ->
            when (modifyProfileState) {
                MyPageViewModel.State.Loading -> Unit
                MyPageViewModel.State.Success -> {
                    myPageViewModel.changeMyPageEvent(
                        MyPageViewModel.Event.ShowToast(getString(R.string.myPage_toast_modify_profile_success))
                    )
                }

                MyPageViewModel.State.Fail -> {
                    myPageViewModel.changeMyPageEvent(
                        MyPageViewModel.Event.ShowToast(getString(R.string.myPage_toast_modify_profile_failed))
                    )
                }
            }
        }
    }

    private fun collectNicknameState() {
        myPageViewModel.nicknameState.collectOnStarted(this) { state ->
            with(binding.tvModifyMyPageNicknameValidateIntroduction) {
                text = getString(state.introduction)
                setTextColor(getColor(state.color))
            }
        }
    }

    private fun collectMyPageEvent() {
        myPageViewModel.myPageEvent.collectOnStarted(this) { event ->
            when (event) {
                is MyPageViewModel.Event.ShowToast -> showToast(event.message)

                MyPageViewModel.Event.ModifyMyPage -> myPageViewModel.patchMyProfile()
                else -> Unit
            }
        }
    }

    private fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun ImageView.loadImageUrl(imageUrl: String?) {
        Glide.with(this)
            .load(imageUrl)
            .into(this)
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, ModifyProfileActivity::class.java)
    }
}
