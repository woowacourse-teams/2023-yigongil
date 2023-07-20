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
    private val studyDetailViewModel: StudyDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        initActionBar()
        initStudyParticipantsList()
    }

    private fun initStudyParticipantsList() {
        val studyPeopleAdapter = StudyParticipantsAdapter()
        binding.rvStudyDetailStudyPeople.setHasFixedSize(true)
        binding.rvStudyDetailStudyPeople.adapter = studyPeopleAdapter
        studyPeopleAdapter.submitList(studyDetailViewModel.studyParticipants)
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

    fun onParticipateButtonClick() {
        studyDetailViewModel.participateStudy()
        binding.btnStudyDetailDm.visibility = View.GONE
        binding.btnStudyDetailParticipate.visibility = View.GONE
        binding.btnStudyDetailWaiting.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val KEY_STUDY_ID = "KEY_STUDY_ID"
        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, StudyDetailActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
            }
    }
}