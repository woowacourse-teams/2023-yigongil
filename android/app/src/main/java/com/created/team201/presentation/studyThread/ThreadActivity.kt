package com.created.team201.presentation.studyThread

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.created.team201.R
import com.created.team201.databinding.ActivityThreadBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.studyThread.adapter.MustDoAdapter
import com.created.team201.presentation.studyThread.adapter.ThreadAdapter
import com.created.team201.presentation.studyThread.uiState.FeedsUiState
import com.created.team201.presentation.studyThread.uiState.MustDoCertificationUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ThreadActivity : BindingActivity<ActivityThreadBinding>(R.layout.activity_thread) {
    private val mustDoAdapter by lazy { MustDoAdapter() }
    private val threadAdapter by lazy { ThreadAdapter() }
    private val threadViewModel: ThreadViewModel by viewModels()
    private val studyId: Long by lazy { intent.getLongExtra(STUDY_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        attachAdapter()
        bindViewModel()
        setupView()

        binding.ivThreadBackButton.setOnClickListener {
            finish()
        }

        binding.ivThreadDirectButton.setOnClickListener {
            threadViewModel.sendFeeds()
            binding.etThreadInput.text.clear()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                threadViewModel.mustDoUiState.collectLatest { uiState ->
                    when (uiState) {
                        is MustDoCertificationUiState.Success -> mustDoAdapter.submitList(uiState.mustDo)
                        is MustDoCertificationUiState.Loading -> {}
                    }
                }

            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                threadViewModel.feedsUiState.collectLatest { uiState ->
                    when (uiState) {
                        is FeedsUiState.Success -> {
                            Log.d("123123123", uiState.feeds.toString())
                            threadAdapter.submitList(uiState.feeds)
                        }

                        is FeedsUiState.Loading -> {}
                    }
                }
            }
        }

    }

    private fun setupView() {
        threadViewModel.apply {
            updateMustDoCertification()
            updateFeeds()
        }
    }

    private fun bindViewModel() {
        binding.lifecycleOwner = this
        binding.vm = threadViewModel.apply {
            updateStudyId(studyId)
        }
    }

    private fun attachAdapter() {
        binding.rvThread.adapter = threadAdapter
        binding.rvThread.setHasFixedSize(true)
        binding.rvMustDo.adapter = mustDoAdapter
        binding.rvMustDo.setHasFixedSize(true)
    }

    companion object {
        private const val STUDY_ID = "STUDY_ID"

        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, ThreadActivity::class.java).apply {
                putExtra(STUDY_ID, studyId)
            }
    }
}