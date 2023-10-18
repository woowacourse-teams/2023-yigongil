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
    }

    private fun setupMemberCertification() {
        val studyId = intent.getLongExtra(KEY_STUDY_ID, -1)
        val certificationId = intent.getLongExtra(KEY_CERTIFICATION_ID, -1)
        certificationCheckViewModel.getMemberCertification(studyId, certificationId)
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
        private const val KEY_STUDY_ID = "STUDY_ID"
        private const val KEY_CERTIFICATION_ID = "CERTIFICATION_ID"
        fun getIntent(context: Context, studyId: Long, certificationId: Long): Intent =
            Intent(context, CertificationCheckActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
                putExtra(KEY_CERTIFICATION_ID, certificationId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
