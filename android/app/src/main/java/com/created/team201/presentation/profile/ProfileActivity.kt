package com.created.team201.presentation.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityProfileBinding
import com.created.team201.presentation.common.BindingActivity

class ProfileActivity : BindingActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    private val profileViewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory }
    private val userId: Long by lazy { intent.getLongExtra(KEY_USER_ID, DEFAULT_USER_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        profileViewModel.initProfile(userId)
    }

    private fun initBinding() {
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = this
    }

    companion object {
        private const val DEFAULT_USER_ID = 0L
        private const val KEY_USER_ID = "KEY_USER_ID"
        fun getIntent(context: Context, userId: Long): Intent =
            Intent(context, ProfileActivity::class.java).apply {
                putExtra(KEY_USER_ID, userId)
            }
    }
}
