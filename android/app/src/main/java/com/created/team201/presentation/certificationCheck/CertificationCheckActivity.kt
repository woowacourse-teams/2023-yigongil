package com.created.team201.presentation.certificationCheck

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityCertificationCheckBinding
import com.created.team201.presentation.certificationCheck.model.MemberCertificationUiState
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.util.BindingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CertificationCheckActivity :
    BindingActivity<ActivityCertificationCheckBinding>(R.layout.activity_certification_check) {

    private val certificationCheckViewModel: CertificationCheckViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupMemberCertification()
        observeMemberCertificationUiState()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.ivCertificationCheckCloseButton.setOnClickListener {
            finish()
        }
    }

    private fun setupMemberCertification() {
        val studyId = intent.getLongExtra(KEY_STUDY_ID, KEY_NOT_FOUND)
        val memberId = intent.getLongExtra(KEY_MEMBER_ID, KEY_NOT_FOUND)
        val roundId = intent.getLongExtra(KEY_ROUND_ID, KEY_NOT_FOUND)
        if ((studyId == KEY_NOT_FOUND) or (memberId == KEY_NOT_FOUND) or (roundId == KEY_NOT_FOUND)) {
            showLoadFailureToast()
            finish()
            return
        }

        certificationCheckViewModel.getMemberCertification(studyId, roundId, memberId)
    }

    private fun observeMemberCertificationUiState() {
        certificationCheckViewModel.uiState.observe(this) { uiState ->
            when (uiState) {
                is MemberCertificationUiState.Success -> {
                    binding.tvCertificationCheckBody.text = uiState.content
                    BindingAdapter.glideSrcUrl(
                        binding.ivCertificationCheckPhoto,
                        uiState.imageUrl,
                    )
                }

                is MemberCertificationUiState.Loading -> Unit
                else -> {
                    showLoadFailureToast()
                    finish()
                }
            }
        }
    }

    private fun showLoadFailureToast() {
        Toast.makeText(
            this,
            getString(R.string.certification_check_fail_loading),
            Toast.LENGTH_SHORT,
        ).show()
    }

    companion object {
        private const val KEY_NOT_FOUND = -1L
        private const val KEY_STUDY_ID = "STUDY_ID"
        private const val KEY_MEMBER_ID = "MEMBER_ID"
        private const val KEY_ROUND_ID = "ROUND_ID"
        fun getIntent(context: Context, studyId: Long, roundId: Long, memberId: Long): Intent =
            Intent(context, CertificationCheckActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
                putExtra(KEY_ROUND_ID, roundId)
                putExtra(KEY_MEMBER_ID, memberId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
