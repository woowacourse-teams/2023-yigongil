package com.created.team201.presentation.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.created.team201.R
import com.created.team201.data.model.UserProfileEntity
import com.created.team201.databinding.ActivityProfileBinding
import com.created.team201.presentation.common.BindingViewActivity
import com.created.team201.presentation.profile.adapter.FinishedStudyAdapter
import com.created.team201.presentation.report.ReportActivity
import com.created.team201.presentation.report.model.ReportCategory
import com.created.team201.util.collectOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity :
    BindingViewActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate) {

    private val profileViewModel: ProfileViewModel by viewModels()
    private val finishedStudyAdapter: FinishedStudyAdapter by lazy { FinishedStudyAdapter() }
    private val userId: Long by lazy { intent.getLongExtra(KEY_USER_ID, NON_EXISTENCE_USER_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initActionBar()
        initProfile()
        initFinishedStudyAdapter()
        observeFinishedStudies()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbProfile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initProfile() {
        profileViewModel.loadProfile(getValidatedUserId())
    }

    private fun getValidatedUserId(): Long {
        if (userId == NON_EXISTENCE_USER_ID) {
            showToast(getString(R.string.profile_unexpected_user_warning))
            finish()
        }
        return userId
    }

    private fun initFinishedStudyAdapter() {
        binding.rvProfileEndedStudies.adapter = finishedStudyAdapter
    }

    private fun observeFinishedStudies() {
        profileViewModel.uiState.collectOnStarted(this) { uiState ->
            when (uiState) {
                is ProfileUiState.Success -> updateProfile(uiState.userProfile)
                is ProfileUiState.Failure -> updateNonFoundProfile()
                is ProfileUiState.Loading -> Unit
            }
        }
    }

    private fun updateProfile(userProfile: UserProfileEntity) {
        binding.ivProfileImage.setImage(userProfile.profile.profileImageUrl)
        binding.tvProfileUserName.text = userProfile.profile.profileInformation.nickname.nickname
        binding.tvProfileUserGithubId.text = userProfile.profile.githubId
        binding.layoutProfileStudySuccessRate.result =
            getString(R.string.profile_success_rate_format).format(userProfile.profile.successRate)
        binding.layoutProfileTodoSuccessRate.result =
            getString(R.string.profile_mustdo_success_rate_format).format(userProfile.profile.successfulRoundCount)
        binding.tvProfileUserDescription.text = userProfile.profile.profileInformation.introduction
        finishedStudyAdapter.submitList(userProfile.finishedStudies)
        binding.layoutProfileTodoSuccessRate.isVisible = true
        binding.layoutProfileStudySuccessRate.isVisible = true
    }

    private fun updateNonFoundProfile() {
        binding.ivProfileImage.setImage(R.drawable.ic_my_page)
        binding.tvProfileUserName.text = getString(R.string.profile_unexpected_user_name)
        binding.viewProfileBorderLine.visibility = INVISIBLE
        binding.viewProfileRateBoardBorderLine.visibility = INVISIBLE
        binding.tvProfileUserDescription.visibility = INVISIBLE
        binding.layoutProfileTodoSuccessRate.isVisible = false
        binding.layoutProfileStudySuccessRate.isVisible = false
        binding.tbProfile.menu.removeItem(R.id.menu_profile_report)
    }

    private fun ImageView.setImage(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(context)
                .load(it)
                .into(this)
        }
    }

    private fun ImageView.setImage(image: Int) {
        Glide.with(context)
            .load(image)
            .into(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val isMyProfile = intent.getBooleanExtra(KEY_MY_PROFILE, false)
        when (isMyProfile) {
            true -> binding.tbProfile.setTitle(R.string.profile_my_title)
            false -> menuInflater.inflate(R.menu.menu_profile, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_profile_report -> {
                if (profileViewModel.isGuest) {
                    showToast(getString(R.string.guest_toast_can_not_report))
                    return true
                }
                startActivity(ReportActivity.getIntent(this, ReportCategory.USER, userId))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    companion object {
        private const val NON_EXISTENCE_USER_ID = 0L
        private const val KEY_USER_ID = "KEY_USER_ID"
        private const val KEY_MY_PROFILE = "KEY_MY_PROFILE"
        fun getIntent(context: Context, userId: Long, isMyProfile: Boolean = false): Intent =
            Intent(context, ProfileActivity::class.java).apply {
                putExtra(KEY_USER_ID, userId)
                putExtra(KEY_MY_PROFILE, isMyProfile)
            }
    }
}
