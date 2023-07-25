package com.created.team201.presentation.studyDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyDetailBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.studyDetail.adapter.StudyParticipantsAdapter

class StudyDetailActivity :
    BindingActivity<ActivityStudyDetailBinding>(R.layout.activity_study_detail) {
    private val studyDetailViewModel: StudyDetailViewModel by viewModels { StudyDetailViewModel.Factory }

    private val userId: Long = TEMP_USER_ID
    private val studyId: Long by lazy { intent.getLongExtra(KEY_STUDY_ID, 0) }

    private val studyPeopleAdapter by lazy { StudyParticipantsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        initActionBar()
        initStudyParticipantsList()
        initStudyDetailInformation()
        observeStudyDetailParticipants()
    }

    private fun initViewModel() {
        binding.viewModel = studyDetailViewModel
        binding.lifecycleOwner = this
        binding.activity = this
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbStudyDetailAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initStudyParticipantsList() {
        binding.rvStudyDetailStudyPeople.setHasFixedSize(true)
        binding.rvStudyDetailStudyPeople.adapter = studyPeopleAdapter
    }

    private fun initStudyDetailInformation() {
        studyDetailViewModel.fetchStudyDetail(userId, studyId)
    }

    fun onParticipateButtonClick() {
        studyDetailViewModel.participateStudy(studyId)
        binding.btnStudyDetailDm.visibility = View.GONE
        binding.btnStudyDetailParticipate.visibility = View.GONE
        binding.btnStudyDetailWaiting.visibility = View.VISIBLE
    }

    fun onStartStudyButtonClick() {
        studyDetailViewModel.startStudy(studyId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeStudyDetailParticipants() {
        studyDetailViewModel.studyParticipants.observe(this) { studyList ->
            studyPeopleAdapter.submitList(studyList)
        }
    }

    companion object {
        private const val TEMP_USER_ID = 1L
        private const val KEY_STUDY_ID = "KEY_STUDY_ID"
        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, StudyDetailActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
            }
    }
}
