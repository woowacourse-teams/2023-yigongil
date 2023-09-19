package com.created.team201.presentation.studyThread

import android.os.Bundle
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityThreadBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.studyThread.adapter.MustDoAdapter
import com.created.team201.presentation.studyThread.adapter.ThreadAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThreadActivity : BindingActivity<ActivityThreadBinding>(R.layout.activity_thread) {
    private val mustDoAdapter by lazy { MustDoAdapter() }
    private val threadAdapter by lazy { ThreadAdapter() }
    private val threadViewModel: ThreadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        attachAdapter()
        bindViewModel()
        binding.ivThreadBackButton.setOnClickListener {
            finish()
        }
    }

    private fun bindViewModel() {
        binding.lifecycleOwner = this
        binding.vm = threadViewModel
    }

    private fun attachAdapter() {
        binding.rvThread.adapter = threadAdapter
        binding.rvThread.setHasFixedSize(true)
        binding.rvMustDo.adapter = mustDoAdapter
        binding.rvMustDo.setHasFixedSize(true)
    }
}