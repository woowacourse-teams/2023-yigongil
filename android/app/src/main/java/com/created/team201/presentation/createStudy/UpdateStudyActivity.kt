package com.created.team201.presentation.createStudy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.created.team201.R
import com.created.team201.databinding.ActivityUpdateStudyBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.createStudy.UpdateStudyViewModel.State.FAIL
import com.created.team201.presentation.createStudy.UpdateStudyViewModel.State.IDLE
import com.created.team201.presentation.createStudy.UpdateStudyViewModel.State.Success
import com.created.team201.presentation.createStudy.bottomSheet.CycleBottomSheetFragment
import com.created.team201.presentation.createStudy.bottomSheet.PeopleCountBottomSheetFragment
import com.created.team201.presentation.createStudy.bottomSheet.PeriodBottomSheetFragment
import com.created.team201.presentation.createStudy.bottomSheet.StartDateBottomSheetFragment
import com.created.team201.presentation.studyDetail.StudyDetailActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UpdateStudyActivity :
    BindingActivity<ActivityUpdateStudyBinding>(R.layout.activity_update_study) {
    private val viewModel: UpdateStudyViewModel by viewModels { UpdateStudyViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initView()
        initActionBar()
        setObserveCreateStudyResult()
    }

    private fun initView() {
        when (intent.getStringExtra(VIEW_MODE)) {
            EDIT_MODE -> {
                binding.tbCreateStudy.title = getString(R.string.createStudy_toolbar_title_edit)

                val studyId = intent.getLongExtra(STUDY_KEY, -1L)
                if (studyId == -1L) throw IllegalArgumentException()
                viewModel.setViewState(studyId)
            }

            CREATE_MODE ->
                binding.tbCreateStudy.title = getString(R.string.createStudy_toolbar_title_create)
        }
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.displayNames = resources.getStringArray(R.array.multiPickerDisplayNames).toList()
        binding.onIconTextButtonClickListener = ::onIconTextButtonClick
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbCreateStudy)
        supportActionBar?.setHomeActionContentDescription(R.string.toolbar_back_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun setObserveCreateStudyResult() {
        viewModel.studyState.observe(this) { studyState ->
            when (studyState) {
                is Success -> navigateToStudyDetail(studyState.studyId)
                is FAIL -> {
                    showToast(getString(R.string.createStudy_toast_fail))
                    finish()
                }

                is IDLE -> throw IllegalStateException()
            }
        }
    }

    private fun navigateToStudyDetail(studyId: Long) {
        startActivity(
            StudyDetailActivity.getIntent(this, studyId),
        )
        finish()
    }

    private fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun onIconTextButtonClick(tag: String) {
        removeAllFragment()
        createBottomSheetFragment(tag)?.show(supportFragmentManager, tag)
    }

    private fun removeAllFragment() {
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.commit {
                remove(it)
            }
        }
    }

    private fun createBottomSheetFragment(tag: String): BottomSheetDialogFragment? {
        return when (tag) {
            getString(R.string.createStudy_tag_people_count) -> {
                PeopleCountBottomSheetFragment()
            }

            getString(R.string.createStudy_tag_start_date) -> {
                StartDateBottomSheetFragment()
            }

            getString(R.string.createStudy_tag_period) -> {
                PeriodBottomSheetFragment()
            }

            getString(R.string.createStudy_tag_cycle) -> {
                CycleBottomSheetFragment()
            }

            else -> {
                null
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> {
                false
            }
        }
    }

    companion object {
        private const val VIEW_MODE = "VIEW_MODE"
        private const val STUDY_KEY = "STUDY_KEY"
        const val CREATE_MODE = "CREATE_MODE"
        const val EDIT_MODE = "EDIT_MODE"

        fun getIntent(context: Context, viewMode: String, studyId: Long?): Intent =
            Intent(context, UpdateStudyActivity::class.java).apply {
                putExtra(VIEW_MODE, viewMode)
                putExtra(STUDY_KEY, studyId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
