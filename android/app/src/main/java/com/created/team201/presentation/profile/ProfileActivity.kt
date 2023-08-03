package com.created.team201.presentation.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.created.team201.R
import com.created.team201.databinding.ActivityProfileBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.profile.adapter.FinishedStudyAdapter

class ProfileActivity : BindingActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    private val profileViewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory }
    private val finishedStudyAdapter: FinishedStudyAdapter by lazy { FinishedStudyAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        profileViewModel.initProfile(getValidatedUserId())
        initFinishedStudyAdapter()
        submitFinishedStudies()
    }

    private fun initBinding() {
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = this
    }

    private fun getValidatedUserId(): Long {
        val userId = intent.getLongExtra(KEY_USER_ID, NON_EXISTENCE_USER_ID)
        if (userId == NON_EXISTENCE_USER_ID) {
            Toast.makeText(
                this,
                this.getString(R.string.profile_unexpected_user_access_warning),
                Toast.LENGTH_SHORT,
            ).show()
            finish()
        }
        return userId
    }

    private fun initFinishedStudyAdapter() {
        binding.rvProfileEndedStudies.adapter = finishedStudyAdapter
        binding.rvProfileEndedStudies.setHasFixedSize(true)
        val dividerItemDecoration = getDividerItemDecoration()
        binding.rvProfileEndedStudies.addItemDecoration(dividerItemDecoration)
    }

    private fun getDividerItemDecoration(): DividerItemDecoration {
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayout.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider_study_list)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        return dividerItemDecoration
    }

    private fun submitFinishedStudies() {
        finishedStudyAdapter.submitList(profileViewModel.profile.value?.finishedStudies)
    }

    companion object {
        private const val NON_EXISTENCE_USER_ID = 0L
        private const val KEY_USER_ID = "KEY_USER_ID"
        fun getIntent(context: Context, userId: Long): Intent =
            Intent(context, ProfileActivity::class.java).apply {
                putExtra(KEY_USER_ID, userId)
            }
    }
}
