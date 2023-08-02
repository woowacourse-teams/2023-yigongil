package com.created.team201.presentation.studyDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyDetailBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.studyDetail.adapter.StudyParticipantsAdapter
import com.created.team201.presentation.studyDetail.model.PeriodFormat

class StudyDetailActivity :
    BindingActivity<ActivityStudyDetailBinding>(R.layout.activity_study_detail),
    StudyMemberClickListener {
    private val studyDetailViewModel: StudyDetailViewModel by viewModels { StudyDetailViewModel.Factory }
    private val userId: Long = TEMP_USER_ID
    private val studyId: Long by lazy { intent.getLongExtra(KEY_STUDY_ID, 0) }
    private val studyPeopleAdapter by lazy { StudyParticipantsAdapter(this) }

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
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initStudyParticipantsList() {
        binding.rvStudyDetailStudyPeople.setHasFixedSize(true)
        binding.rvStudyDetailStudyPeople.adapter = studyPeopleAdapter
    }

    private fun initStudyDetailInformation() {
        studyDetailViewModel.fetchStudyDetail(userId, studyId)
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

    fun convertPeriodOfCountFormat(periodOfCount: String?): String {
        val stringRes =
            PeriodFormat.valueOf(periodOfCount?.last() ?: NON_EXISTENCE_PERIOD_SYMBOL).res
        return getString(stringRes, periodOfCount?.dropLast(STRING_LAST_INDEX)?.toInt())
    }

    fun initMainButtonOnClick(isMaster: Boolean) {
        if (isMaster) return onMasterClickMainButton()
        return onNothingClickMainButton()
    }

    private fun onMasterClickMainButton() {
        studyDetailViewModel.startStudy(studyId)
    }

    private fun onNothingClickMainButton() {
        studyDetailViewModel.participateStudy(studyId)
    }

    override fun onAcceptApplicantClick(memberId: Long) {
        studyDetailViewModel.acceptApplicant(studyId, memberId)
    }

    companion object {
        private const val NON_EXISTENCE_PERIOD_SYMBOL = 'z'
        private const val STRING_LAST_INDEX = 1
        private const val TEMP_USER_ID = 1L
        private const val KEY_STUDY_ID = "KEY_STUDY_ID"
        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, StudyDetailActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
            }
    }
}
