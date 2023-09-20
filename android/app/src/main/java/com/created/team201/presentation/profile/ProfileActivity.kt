package com.created.team201.presentation.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.created.team201.R
import com.created.team201.databinding.ActivityProfileBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.profile.adapter.FinishedStudyAdapter
import com.created.team201.presentation.report.ReportActivity
import com.created.team201.presentation.report.model.ReportCategory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BindingActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    private val profileViewModel: ProfileViewModel by viewModels()
    private val finishedStudyAdapter: FinishedStudyAdapter by lazy { FinishedStudyAdapter() }
    private val userId: Long by lazy { intent.getLongExtra(KEY_USER_ID, NON_EXISTENCE_USER_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initActionBar()
        profileViewModel.initProfile(getValidatedUserId())
        initFinishedStudyAdapter()
        submitFinishedStudies()
    }

    private fun initBinding() {
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = this
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbProfile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun getValidatedUserId(): Long {
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
        ContextCompat.getDrawable(this, R.drawable.divider_recyclerview_line)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        return dividerItemDecoration
    }

    private fun submitFinishedStudies() {
        profileViewModel.profile.observe(this) {
            finishedStudyAdapter.submitList(profileViewModel.profile.value?.finishedStudies)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_profile_report -> {
                startActivity(ReportActivity.getIntent(this, ReportCategory.USER, userId))
            }
        }
        return super.onOptionsItemSelected(item)
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
