package com.created.team201.presentation.studyManagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.created.domain.model.PageIndex
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyManagementBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.studyManagement.adapter.StudyManagementAdapter

class StudyManagementActivity :
    BindingActivity<ActivityStudyManagementBinding>(R.layout.activity_study_management) {

    private val studyManagementViewModel: StudyManagementViewModel by viewModels { StudyManagementViewModel.Factory }
    private val studyManagementAdapter: StudyManagementAdapter by lazy {
        StudyManagementAdapter(studyManagementClickListener, memberClickListener)
    }
    private val studyId: Long by lazy { intent.getLongExtra(KEY_STUDY_ID, KEY_ERROR) }
    private val roundId: Long by lazy { intent.getLongExtra(KEY_ROUND_ID, KEY_ERROR) }

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
        studyManagementViewModel.fetchStudyInformation(studyId)
    }

    private fun initStudyRounds() {
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

                    studyManagementViewModel.updateCurrentPage(PageIndex(position))
                    setPageChangeButtonEnabled()
                }
            },
        )
    }

    private fun setPageChangeButtonEnabled() {
        binding.ivStudyManagementPreviousButton.isEnabled =
            studyManagementViewModel.currentRound.value != FIRST_ROUND
        binding.ivStudyManagementNextButton.isEnabled =
            studyManagementViewModel.currentRound.value != studyManagementViewModel.studyInformation.totalRoundCount
    }

    private val studyManagementClickListener = object : StudyManagementClickListener {
        override fun clickOnTodo(id: Long, isDone: Boolean) {
            val currentItemId = binding.vpStudyManagement.currentItem
            studyManagementViewModel.updateTodo(currentItemId, id, !isDone, studyId)
        }

        override fun onClickAddTodo(todoContent: String) {
            if (todoContent.isEmpty()) {
                toastEmptyTodoInput()
                return
            }
            val currentPage = binding.vpStudyManagement.currentItem
            val studyId = intent.getLongExtra(KEY_STUDY_ID, KEY_ERROR)
            studyManagementViewModel.addOptionalTodo(studyId, currentPage, todoContent)
        }

        override fun onClickAddOptionalTodo(optionalTodoCount: Int) {
            if (optionalTodoCount >= MAXIMUM_OPTIONAL_TODO_COUNT) {
                Toast.makeText(
                    this@StudyManagementActivity,
                    getString(R.string.study_management_not_allowed_add_optional_todo),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun toastEmptyTodoInput() {
        Toast.makeText(
            this@StudyManagementActivity,
            getString(R.string.study_management_not_allowed_empty_content),
            Toast.LENGTH_SHORT,
        ).show()
    }

    private val memberClickListener = object : StudyMemberClickListener {
        override fun onClickMember(id: Long) {
            // 프로필 페이지로 이동
        }
    }

    private fun initPageButtonClickListener() {
        binding.ivStudyManagementPreviousButton.setOnClickListener {
            val page = PageIndex(binding.vpStudyManagement.currentItem).decrease()
            binding.vpStudyManagement.setCurrentItem(page.number, true)
            studyManagementViewModel.fetchRoundDetail(studyId, page)
        }
        binding.ivStudyManagementNextButton.setOnClickListener {
            val page =
                PageIndex(binding.vpStudyManagement.currentItem).increase(studyManagementAdapter.itemCount - 1)
            binding.vpStudyManagement.setCurrentItem(page.number, true)
            studyManagementViewModel.fetchRoundDetail(studyId, page)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val FIRST_ROUND = 1
        private const val MAXIMUM_OPTIONAL_TODO_COUNT = 4
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
