package com.created.team201.presentation.studyManagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyManagementBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.studyManagement.adapter.StudyManagementAdapter

class StudyManagementActivity :
    BindingActivity<ActivityStudyManagementBinding>(R.layout.activity_study_management) {

    private val studyManagementViewModel by viewModels<StudyManagementViewModel>()
    private val studyManagementAdapter: StudyManagementAdapter by lazy {
        StudyManagementAdapter(studyManagementClickListener, memberClickListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initActionBar()
        initStudyInformation()
        initStudyRounds()
        initAdapter()
        initPageButtonClickListener()
        observeStudyManagement()
    }

    private fun initViewModel() {
        binding.viewModel = studyManagementViewModel
        binding.lifecycleOwner = this
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbStudyManagement)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initStudyInformation() {
        studyManagementViewModel.fetchStudyInformation()
    }

    private fun initStudyRounds() {
        val studyId = intent.getLongExtra(KEY_STUDY_ID, KEY_ERROR)
        val roundId = intent.getLongExtra(KEY_ROUND_ID, KEY_ERROR)
        studyManagementViewModel.getStudyRounds(studyId, roundId)
    }

    private fun initAdapter() {
        binding.vpStudyManagement.adapter = studyManagementAdapter
    }

    private fun observeStudyManagement() {
        studyManagementViewModel.studyRounds.observe(this) { studyRoundDetails ->
            studyManagementAdapter.submitList(studyRoundDetails)
        }
        binding.vpStudyManagement.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    studyManagementViewModel.updateCurrentPage(position)
                    setPageChangeButtonEnabled()
                }
            },
        )
    }

    private fun setPageChangeButtonEnabled() {
        binding.ivStudyManagementPreviousButton.isEnabled =
            studyManagementViewModel.currentRound.value != 1
        binding.ivStudyManagementNextButton.isEnabled =
            studyManagementViewModel.currentRound.value != studyManagementViewModel.studyInformation.totalRoundCount
    }

    private val studyManagementClickListener = object : StudyManagementClickListener {
        override fun clickOnTodo(id: Long, isDone: Boolean) {
            val currentItemId = binding.vpStudyManagement.currentItem
            studyManagementViewModel.updateTodo(currentItemId, id, !isDone)
        }

        override fun onClickAddTodo(todoContent: String) {
            val currentPage = binding.vpStudyManagement.currentItem
            val studyId = intent.getLongExtra(KEY_STUDY_ID, KEY_ERROR)
            studyManagementViewModel.addOptionalTodo(studyId, currentPage, todoContent)
        }
    }

    private val memberClickListener = object : StudyMemberClickListener {
        override fun onClickMember(id: Long) {
            // 프로필 페이지로 이동
        }
    }

    private fun initPageButtonClickListener() {
        binding.ivStudyManagementPreviousButton.setOnClickListener {
            val page = (binding.vpStudyManagement.currentItem - 1).coerceAtLeast(0)
            binding.vpStudyManagement.setCurrentItem(page, true)
            studyManagementViewModel.fetchRoundDetail(page)
        }
        binding.ivStudyManagementNextButton.setOnClickListener {
            val page =
                (binding.vpStudyManagement.currentItem + 1).coerceAtMost(studyManagementAdapter.itemCount - 1)
            binding.vpStudyManagement.setCurrentItem(page, true)
            studyManagementViewModel.fetchRoundDetail(page)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val KEY_ERROR = 0L
        private const val KEY_STUDY_ID = "KEY_STUDY_ID"
        private const val KEY_ROUND_ID = "KEY_ROUND_ID"
        fun getIntent(context: Context, studyId: Long, roundId: Long): Intent =
            Intent(context, StudyManagementActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
                putExtra(KEY_ROUND_ID, roundId)
            }
    }
}
