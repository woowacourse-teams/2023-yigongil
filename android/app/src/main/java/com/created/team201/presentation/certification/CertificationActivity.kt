package com.created.team201.presentation.certification

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
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
    }
}
