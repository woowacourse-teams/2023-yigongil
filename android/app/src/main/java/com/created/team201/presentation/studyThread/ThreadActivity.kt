package com.created.team201.presentation.studyThread

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ActivityThreadBinding
import com.created.team201.presentation.certification.CertificationActivity
import com.created.team201.presentation.certificationCheck.CertificationCheckActivity
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.profile.ProfileActivity
import com.created.team201.presentation.studyThread.ThreadUiState.Loading
import com.created.team201.presentation.studyThread.ThreadUiState.Success
import com.created.team201.presentation.studyThread.adapter.MoreAdapter
import com.created.team201.presentation.studyThread.adapter.MustDoAdapter
import com.created.team201.presentation.studyThread.adapter.ThreadAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ThreadActivity : BindingActivity<ActivityThreadBinding>(R.layout.activity_thread) {
    private val mustDoAdapter: MustDoAdapter by lazy { MustDoAdapter(::onMemberCertificationClick) }
    private val threadAdapter: ThreadAdapter by lazy { ThreadAdapter(::onUserClick) }
    private val moreAdapter: MoreAdapter by lazy {
        MoreAdapter(
            this,
            R.layout.item_thread_more,
            ::onClickMore,
        )
    }
    private val threadViewModel: ThreadViewModel by viewModels()
    private val studyId: Long by lazy { intent.getLongExtra(STUDY_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        threadViewModel.initStudyThread(studyId)
        setupThreadAdapter()
        attachAdapter()
        setOnClickEvent()
        collectUiState()
    }

    override fun onRestart() {
        super.onRestart()

        threadViewModel.updateMustDoCertification()
    }

    private fun setupThreadAdapter() {
        threadAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvThread.scrollToPosition(SCROLL_TO_BOTTOM_IDX)
            }
        })
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
        binding.tvThreadStudyName.text = state.studyName
        mustDoAdapter.submitList(state.mustDo)
        threadAdapter.submitList(state.feeds)
    }

    private fun setOnClickEvent() {
        binding.ivThreadBackButton.setOnClickListener {
            finish()
        }

        binding.ivThreadDirectButton.setOnClickListener {
            val message = binding.etThreadInput.text.toString()

            if (message.isBlank()) {
                showBlankInputToast()
                return@setOnClickListener
            }

            threadViewModel.dispatchFeed(message)
            binding.etThreadInput.run {
                text.clear()
                clearFocus()
                hideKeyboard()
            }
        }
    }

    private fun showBlankInputToast() {
        Toast.makeText(
            this,
            getText(R.string.thread_blank_input_toast_title),
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun onMemberCertificationClick(certificationId: Long) {
        startActivity(
            CertificationCheckActivity.getIntent(this, studyId, certificationId),
        )
    }

    private fun onUserClick(memberId: Long) {
        val isMyProfile = threadViewModel.isMyProfile(memberId)
        startActivity(
            ProfileActivity.getIntent(this, memberId, isMyProfile)
                .also { it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP },
        )
    }

    private fun onClickMore(position: Int) {
        when (position) {
            MUST_DO -> {
                // createBottomDialog
            }

            MUST_DO_CERTIFICATION -> {
                showCertification()
            }

            STUDY_INFO -> {
                showStudyInformationDialog()
            }
        }
        hideSpinnerDropDown(binding.spinnerThread)
    }

    private fun hideSpinnerDropDown(spinner: Spinner) {
        runCatching {
            val method = Spinner::class.java.getDeclaredMethod("onDetachedFromWindow")
            method.isAccessible = true
            method.invoke(spinner)
        }
    }

    private fun showCertification() {
        if (threadViewModel.uiState.value !is Success) return
        when ((threadViewModel.uiState.value as Success).mustDo[MY_MUST_DO_IDX].isCertified) {
            true -> showCertificationIsDoneToast()
            false -> startActivity(CertificationActivity.getIntent(this, studyId))
        }
    }

    private fun showCertificationIsDoneToast() {
        Toast.makeText(this, R.string.certification_post_is_done_message, Toast.LENGTH_SHORT).show()
    }

    private fun showStudyInformationDialog() {
        StudyInformationDialog(this, threadViewModel.studyDetail).show()
    }

    private fun attachAdapter() {
        binding.rvThread.adapter = threadAdapter
        binding.rvThread.setHasFixedSize(true)
        binding.rvMustDo.adapter = mustDoAdapter
        binding.rvMustDo.setHasFixedSize(true)
        binding.spinnerThread.adapter = moreAdapter
    }

    private fun View.hideKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    companion object {
        private const val STUDY_ID = "STUDY_ID"
        private const val MUST_DO = 0
        private const val MUST_DO_CERTIFICATION = 1
        private const val STUDY_INFO = 2
        private const val SCROLL_TO_BOTTOM_IDX = 0
        private const val MY_MUST_DO_IDX = 0

        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, ThreadActivity::class.java).apply {
                putExtra(STUDY_ID, studyId)
            }
    }
}
