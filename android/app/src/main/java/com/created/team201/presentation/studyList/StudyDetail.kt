package com.created.team201.presentation.studyList

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyDetailBinding
import com.created.team201.presentation.common.BindingActivity

class StudyDetail : BindingActivity<ActivityStudyDetailBinding>(R.layout.activity_study_detail) {
    private val viewModel: StudyDetailViewModel by viewModels()

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
        studyPeopleAdapter.submitList(viewModel.studyParticipants)
    }

    private fun initViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbStudyDetailAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
