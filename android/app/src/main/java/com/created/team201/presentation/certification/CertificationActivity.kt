package com.created.team201.presentation.certification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityCertificationBinding
import com.created.team201.presentation.common.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CertificationActivity :
    BindingActivity<ActivityCertificationBinding>(R.layout.activity_certification) {

    private val certificationViewModel: CertificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupCloseButtonListener()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
    }

    private fun setupCloseButtonListener() {
        binding.ivCertificationCloseButton.setOnClickListener {
            finish()
        }
    }

    companion object {
        private const val KEY_STUDY_ID = "STUDY_ID"
        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, CertificationActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
