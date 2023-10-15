package com.created.team201.presentation.studyThread

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.created.team201.R
import com.created.team201.databinding.ActivityThreadBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.studyThread.ThreadUiState.Loading
import com.created.team201.presentation.studyThread.ThreadUiState.Success
import com.created.team201.presentation.studyThread.adapter.MustDoAdapter
import com.created.team201.presentation.studyThread.adapter.ThreadAdapter
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

        bindViewModel()
        attachAdapter()
        setOnClickEvent()
        collectUiState()
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                threadViewModel.uiState.collectLatest { state ->
                    when (state) {
                        is Success -> showView(state)
                        is Loading -> {}
                    }
                }
            }
        }
    }

    private fun showView(state: Success) {
        threadAdapter.submitList(state.feeds)
        mustDoAdapter.submitList(state.mustDo)
    }

    private fun setOnClickEvent() {
        binding.ivThreadBackButton.setOnClickListener {
            finish()
        }

        binding.ivThreadDirectButton.setOnClickListener {
            val message = binding.etThreadInput.text.toString()

            threadViewModel.dispatchFeed(message)
            binding.etThreadInput.text.clear()
        }
    }

    private fun bindViewModel() {
        binding.lifecycleOwner = this
        binding.vm = threadViewModel.also {
            it.updateStudyId(studyId)
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