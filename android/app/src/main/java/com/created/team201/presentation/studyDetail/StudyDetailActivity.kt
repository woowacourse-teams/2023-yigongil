package com.created.team201.presentation.studyDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.fragment.app.commit
import com.created.domain.model.Role
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyDetailBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.guest.GuestViewModel
import com.created.team201.presentation.guest.bottomSheet.LoginBottomSheetFragment
import com.created.team201.presentation.profile.ProfileActivity
import com.created.team201.presentation.report.ReportActivity
import com.created.team201.presentation.report.model.ReportCategory
import com.created.team201.presentation.studyDetail.StudyDetailState.Guest
import com.created.team201.presentation.studyDetail.StudyDetailState.Master
import com.created.team201.presentation.studyDetail.StudyDetailViewModel.UIState.Loading
import com.created.team201.presentation.studyDetail.StudyDetailViewModel.UIState.Success
import com.created.team201.presentation.studyDetail.adapter.StudyParticipantsAdapter
import com.created.team201.presentation.studyDetail.bottomSheet.StudyStartBottomSheetFragment
import com.created.team201.presentation.studyDetail.model.StudyDetailUIModel
import com.created.team201.presentation.studyThread.ThreadActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyDetailActivity :
    BindingActivity<ActivityStudyDetailBinding>(R.layout.activity_study_detail),
    StudyMemberClickListener {
    private val studyDetailViewModel: StudyDetailViewModel by viewModels()
    private val guestViewModel: GuestViewModel by viewModels()
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
        observeGuestState()
        observeStudyDetailParticipants()
        observeStartStudy()
        observeCanStartStudy()
        observeParticipantsCount()
        setClickEventOnSub()
    }

    private fun setClickEventOnSub() {
        binding.btnStudyDetailSub.setOnClickListener {
            when (studyDetailViewModel.state.value) {
                is Master -> {
                    showToast(R.string.study_detail_button_preparing_service)
                    navigateToEditStudyView()
                }

                is Guest -> showLoginBottomSheetDialog()
                else -> Unit
            }
        }
    }

    private fun navigateToEditStudyView() {
        // TODO 스터디 수정 뷰 어떻게 할건지 논의 후 화면 이동
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
            android.R.id.home -> {
                finish()
            }

            R.id.menu_study_detail_report -> {
                when (studyDetailViewModel.state.value) {
                    is Guest -> showLoginBottomSheetDialog()
                    else ->
                        startActivity(ReportActivity.getIntent(this, ReportCategory.STUDY, studyId))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeGuestState() {
        guestViewModel.refreshState.observe(this) { signUpGuestState ->
            when (signUpGuestState) {
                true -> studyDetailViewModel.refresh(studyId)
                false -> Unit
            }
        }
    }

    private fun observeStudyDetailParticipants() {
        studyDetailViewModel.studyParticipants.observe(this) { studyList ->
            studyPeopleAdapter.submitList(studyList)
        }
    }

    fun initMainButtonOnClick(role: Role) {
        when (role) {
            Role.MASTER -> onMasterClickMainButton()
            Role.GUEST -> showLoginBottomSheetDialog()
            else -> onNothingClickMainButton()
        }
    }

    private fun onMasterClickMainButton() {
        val studyStartBottomSheetFragment = StudyStartBottomSheetFragment.newInstance(studyId)
        studyStartBottomSheetFragment.show(
            supportFragmentManager,
            studyStartBottomSheetFragment.tag,
        )
    }

    private fun onNothingClickMainButton() {
        studyDetailViewModel.participateStudy(studyId)
    }

    private fun showLoginBottomSheetDialog() {
        removeAllFragment()
        LoginBottomSheetFragment().show(
            supportFragmentManager,
            LoginBottomSheetFragment.TAG_LOGIN_BOTTOM_SHEET,
        )
    }

    private fun removeAllFragment() {
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.commit {
                remove(it)
            }
        }
    }

    override fun onAcceptApplicantClick(memberId: Long) {
        if (studyDetailViewModel.isFullMember.value) {
            showToast(R.string.study_detail_do_not_accept_member_anymore)
            return
        }
        studyDetailViewModel.acceptApplicant(studyId, memberId)
    }

    override fun onUserClick(memberId: Long) {
        if (studyDetailViewModel.state.value is Guest) {
            startActivity(ProfileActivity.getIntent(this, memberId))
            return
        }

        val isMyProfile = studyDetailViewModel.myProfile.id == memberId
        startActivity(ProfileActivity.getIntent(this, memberId, isMyProfile))
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
        studyDetailViewModel.startStudyState.observe(this) { startStudyState ->
            when (startStudyState) {
                Success -> navigateStudyThread()
                Loading -> Unit
                else -> showToast(R.string.study_detail_toast_study_start_failed)
            }
        }
    }

    private fun observeCanStartStudy() {
        studyDetailViewModel.canStudyStart.observe(this) { cantStartStudy ->
            binding.btnStudyDetailMain.isEnabled = cantStartStudy
        }
    }

    private fun navigateStudyThread() {
        startActivity(ThreadActivity.getIntent(this, studyId))
        finish()
    }


    private fun showToast(@StringRes stringRes: Int) =
        Toast.makeText(this, getString(stringRes), Toast.LENGTH_SHORT).show()

    companion object {
        private const val NON_EXISTENCE_STUDY_ID = 0L
        private const val KEY_STUDY_ID = "KEY_STUDY_ID"
        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, StudyDetailActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
