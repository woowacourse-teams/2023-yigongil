package com.created.team201.presentation.studyDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyDetailBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.profile.ProfileActivity
import com.created.team201.presentation.studyDetail.StudyDetailState.Master
import com.created.team201.presentation.studyDetail.adapter.StudyParticipantsAdapter
import com.created.team201.presentation.studyDetail.model.PeriodFormat
import com.created.team201.presentation.studyDetail.model.StudyDetailUIModel
import com.created.team201.presentation.studyManagement.StudyManagementActivity
import com.created.team201.presentation.updateStudy.UpdateStudyActivity

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
        observeCanStartStudy()
        observeParticipantsCount()
        setClickEventOnSub()
    }

    private fun setClickEventOnSub() {
        binding.btnStudyDetailSub.setOnClickListener {
            if (studyDetailViewModel.state.value is Master) {
                navigateToEditStudyView()
            }
        }
    }

    private fun navigateToEditStudyView() {
        startActivity(
            UpdateStudyActivity.getIntent(
                context = this,
                viewMode = UpdateStudyActivity.EDIT_MODE,
                studyId = studyId,
            ),
        )
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
            showToast(R.string.study_detail_notify_invalid_study)
            finish()
        }
    }

    private fun initStudyParticipantsList() {
        binding.rvStudyDetailStudyPeople.setHasFixedSize(true)
        binding.rvStudyDetailStudyPeople.adapter = studyPeopleAdapter
    }

    private fun initStudyDetailInformation() {
        studyDetailViewModel.fetchStudyDetail(studyId) {
            if (studyDetailViewModel.study.value == StudyDetailUIModel.INVALID_STUDY_DETAIL) {
                showToast(R.string.study_detail_notify_invalid_study)
            }
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_study_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_study_detail_report -> {
                showToast(R.string.study_detail_not_yet)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeStudyDetailParticipants() {
        studyDetailViewModel.studyParticipants.observe(this) { studyList ->
            studyPeopleAdapter.submitList(studyList)
        }
    }

    fun convertPeriodOfCountFormat(periodOfCount: String): String {
        if (periodOfCount == "") return ""
        val stringRes =
            PeriodFormat.valueOf(periodOfCount.last()).res
        return getString(stringRes, periodOfCount.dropLast(STRING_LAST_INDEX).toInt())
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
        if (studyDetailViewModel.isFullMember.value) {
            showToast(R.string.study_detail_do_not_accept_member_anymore)
            return
        }
        studyDetailViewModel.acceptApplicant(studyId, memberId)
    }

    override fun onUserClick(memberId: Long) {
        startActivity(ProfileActivity.getIntent(this, memberId))
    }

    private fun observeParticipantsCount() {
        studyDetailViewModel.studyMemberCount.observe(this) {
            if (studyDetailViewModel.state.value is Master) {
                binding.btnStudyDetailMain.text =
                    getString(
                        R.string.study_detail_button_start_study,
                        studyDetailViewModel.studyMemberCount.value,
                        studyDetailViewModel.study.value.peopleCount,
                    )
            }
        }
    }

    private fun observeStartStudy() {
        studyDetailViewModel.isStartStudy.observe(this) { isStartStudy ->
            if (isStartStudy) {
                startActivity(
                    StudyManagementActivity.getIntent(this, studyId, ROLE_INDEX_STUDY_MASTER),
                )
                finish()
            }
        }
    }

    private fun observeCanStartStudy() {
        studyDetailViewModel.canStudyStart.observe(this) { cantStartStudy ->
            binding.btnStudyDetailMain.isEnabled = cantStartStudy
        }
    }

    private fun showToast(@StringRes stringRes: Int) =
        Toast.makeText(this, getString(stringRes), Toast.LENGTH_SHORT).show()

    companion object {
        private const val ROLE_INDEX_STUDY_MASTER = 0
        private const val NON_EXISTENCE_STUDY_ID = 0L
        private const val STRING_LAST_INDEX = 1
        private const val KEY_STUDY_ID = "KEY_STUDY_ID"
        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, StudyDetailActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
