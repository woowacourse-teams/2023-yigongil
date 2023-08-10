package com.created.team201.presentation.studyManagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.OVER_SCROLL_NEVER
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.created.domain.model.PageIndex
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyManagementBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.profile.ProfileActivity
import com.created.team201.presentation.studyManagement.StudyManagementActivity.TodoEditState.FAILURE
import com.created.team201.presentation.studyManagement.StudyManagementActivity.TodoEditState.SUCCESS
import com.created.team201.presentation.studyManagement.TodoState.DEFAUTL
import com.created.team201.presentation.studyManagement.TodoState.NECESSARY_TODO_EDIT
import com.created.team201.presentation.studyManagement.TodoState.NOTHING
import com.created.team201.presentation.studyManagement.adapter.StudyManagementAdapter
import com.created.team201.presentation.studyManagement.custom.StudyInformationDialog

class StudyManagementActivity :
    BindingActivity<ActivityStudyManagementBinding>(R.layout.activity_study_management) {

    private val studyManagementViewModel: StudyManagementViewModel by viewModels { StudyManagementViewModel.Factory }
    private val studyManagementAdapter: StudyManagementAdapter by lazy {
        StudyManagementAdapter(studyManagementClickListener, memberClickListener)
    }
    private val studyId: Long by lazy { intent.getLongExtra(KEY_STUDY_ID, KEY_ERROR_LONG) }
    private val roleIndex: Int by lazy { intent.getIntExtra(KEY_ROLE_INDEX, KEY_ERROR_ROLE_INT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initActionBar()
        initStudyInformation()
        initViewPager()
        initPage()
        initPageButtonClickListener()
        observeStudyManagement()
    }

    private fun initViewModel() {
        binding.viewModel = studyManagementViewModel
        binding.lifecycleOwner = this
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbStudyManagement)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            setHomeActionContentDescription(R.string.toolbar_back_text)
        }
    }

    private fun initStudyInformation() {
        studyManagementViewModel.initStudyManagement(studyId, roleIndex)
    }

    private fun initViewPager() {
        binding.vpStudyManagement.apply {
            adapter = studyManagementAdapter
            getChildAt(PAGE_INDEX_ZERO).overScrollMode = OVER_SCROLL_NEVER
        }
    }

    private fun initPage() {
        studyManagementViewModel.isStudyRoundsLoaded.observe(this) { isStudyRoundsLoaded ->
            if (!isStudyRoundsLoaded) return@observe
            val currentRound = studyManagementViewModel.currentRound.value ?: FIRST_ROUND
            binding.vpStudyManagement.setCurrentItem(currentRound - CONVERT_TO_PAGE, false)
            binding.skeletonStudyManagement.clItemStudyManagementSkeleton.visibility = GONE
        }
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

        override fun onClickAddTodo(isNecessary: Boolean, todoContent: String) {
            val trimmedTodoContent = todoContent.trim()
            if (trimmedTodoContent.isEmpty() || trimmedTodoContent.isBlank()) {
                toastEmptyTodoInput()
                return
            }
            studyManagementViewModel.addTodo(isNecessary, trimmedTodoContent)
        }

        override fun onClickUpdateTodoIsDone(isNecessary: Boolean, todoId: Long, isDone: Boolean) {
            studyManagementViewModel.updateTodoIsDone(isNecessary, todoId, isDone)
        }

        override fun onClickGenerateOptionalTodo(optionalTodoCount: Int) {
            if (optionalTodoCount >= MAXIMUM_OPTIONAL_TODO_COUNT) {
                Toast.makeText(
                    this@StudyManagementActivity,
                    getString(R.string.study_management_not_allowed_add_optional_todo),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        override fun onClickEditNecessaryTodo(todoContent: String): TodoState {
            return when (studyManagementViewModel.todoState.value) {
                NECESSARY_TODO_EDIT -> {
                    when (updateTodoContent(true, todoContent)) {
                        SUCCESS -> DEFAUTL
                        FAILURE -> NECESSARY_TODO_EDIT
                    }
                }

                DEFAUTL -> {
                    studyManagementViewModel.setTodoState(NECESSARY_TODO_EDIT)
                    NECESSARY_TODO_EDIT
                }

                else -> NOTHING
            }
        }
    }

    private fun updateTodoContent(isNecessary: Boolean, todoContent: String): TodoEditState {
        val trimmedTodoContent = todoContent.trim()
        if (trimmedTodoContent.isEmpty() || trimmedTodoContent.isBlank()) {
            toastEmptyTodoInput()
            return FAILURE
        }
        val currentPage = binding.vpStudyManagement.currentItem
        studyManagementViewModel.updateTodoContent(
            isNecessary,
            trimmedTodoContent,
        )
        return SUCCESS
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
            startActivity(ProfileActivity.getIntent(this@StudyManagementActivity, id))
        }
    }

    private fun initPageButtonClickListener() {
        binding.ivStudyManagementPreviousButton.setOnClickListener {
            val page = PageIndex(binding.vpStudyManagement.currentItem).decrease()
            binding.vpStudyManagement.setCurrentItem(page.number, true)
        }
        binding.ivStudyManagementNextButton.setOnClickListener {
            val page =
                PageIndex(binding.vpStudyManagement.currentItem).increase(studyManagementAdapter.itemCount - 1)
            binding.vpStudyManagement.setCurrentItem(page.number, true)
        }
    }

    private fun showStudyInformationDialog() {
        StudyInformationDialog(this, studyManagementViewModel.studyInformation).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_study_management, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_study_management_information -> {
                showStudyInformationDialog()
                true
            }

            else -> false
        }
    }

    sealed interface TodoEditState {

        object SUCCESS : TodoEditState

        object FAILURE : TodoEditState
    }

    companion object {
        private const val FIRST_ROUND = 1
        private const val CONVERT_TO_PAGE = 1
        private const val PAGE_INDEX_ZERO = 0
        private const val MAXIMUM_OPTIONAL_TODO_COUNT = 4
        private const val KEY_ERROR_LONG = 0L
        private const val KEY_ERROR_ROLE_INT = 3
        private const val KEY_STUDY_ID = "KEY_STUDY_ID"
        private const val KEY_ROLE_INDEX = "KEY_ROLE_INDEX"

        fun getIntent(context: Context, studyId: Long, roleIndex: Int): Intent =
            Intent(context, StudyManagementActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
                putExtra(KEY_ROLE_INDEX, roleIndex)
            }
    }
}
