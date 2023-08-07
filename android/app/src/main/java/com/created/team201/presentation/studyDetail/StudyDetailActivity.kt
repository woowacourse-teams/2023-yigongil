package com.created.team201.presentation.studyDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyDetailBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.profile.ProfileActivity
import com.created.team201.presentation.studyDetail.adapter.StudyParticipantsAdapter
import com.created.team201.presentation.studyDetail.model.PeriodFormat
import com.created.team201.presentation.studyManagement.StudyManagementActivity

class StudyDetailActivity :
    BindingActivity<ActivityStudyDetailBinding>(R.layout.activity_study_detail),
    StudyMemberClickListener {
    private val studyDetailViewModel: StudyDetailViewModel by viewModels { StudyDetailViewModel.Factory }
    private val studyId: Long by lazy { intent.getLongExtra(KEY_STUDY_ID, NON_EXISTENCE_STUDY_ID) }
    private val studyPeopleAdapter by lazy { StudyParticipantsAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        initActionBar()
        validateStudyId()
        initStudyParticipantsList()
        initStudyDetailInformation()
        observeStudyDetailParticipants()
        observeStartStudy()
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
        supportActionBar?.setHomeActionContentDescription(R.string.toolbar_back_text)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun validateStudyId() {
        if (studyId == NON_EXISTENCE_STUDY_ID) {
            Toast.makeText(this, "스터디를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initStudyParticipantsList() {
        binding.rvStudyDetailStudyPeople.setHasFixedSize(true)
        binding.rvStudyDetailStudyPeople.adapter = studyPeopleAdapter
    }

    private fun initStudyDetailInformation() {
        studyDetailViewModel.fetchStudyDetail(studyId)
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
            PeriodFormat.valueOf(periodOfCount?.last() ?: DEFAULT_PERIOD_SYMBOL).res
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
        if (studyDetailViewModel.isFullMember.value == true) {
            Toast.makeText(
                this,
                getString(R.string.study_detail_do_not_accept_member_anymore),
                Toast.LENGTH_SHORT,
            ).show()
            return
        }
        studyDetailViewModel.acceptApplicant(studyId, memberId)
    }

    override fun onUserClick(memberId: Long) {
        startActivity(ProfileActivity.getIntent(this, memberId))
    }

    private fun observeStartStudy() {
        studyDetailViewModel.isStartStudy.observe(this) { isStartStudy ->
            if (isStartStudy) {
                startActivity(
                    StudyManagementActivity.getIntent(
                        this,
                        studyId,
                        FIRST_PAGE,
                        ROLE_INDEX_STUDY_MASTER,
                    ),
                )
                finish()
            }
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val ROLE_INDEX_STUDY_MASTER = 0
        private const val NON_EXISTENCE_STUDY_ID = 0L
        private const val DEFAULT_PERIOD_SYMBOL = 'd'
        private const val STRING_LAST_INDEX = 1
        private const val KEY_STUDY_ID = "KEY_STUDY_ID"
        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, StudyDetailActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
            }
    }
}
