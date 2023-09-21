package com.created.team201.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.created.team201.R
import com.created.team201.databinding.FragmentHomeBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.home.HomeViewModel.UserStudyState.Joined
import com.created.team201.presentation.home.adapter.HomeAdapter
import com.created.team201.presentation.studyThread.ThreadActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter(::navigateToThreadActivity) }
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupViewModel()
        collectUiState()
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = homeViewModel
    }

    private fun navigateToThreadActivity(studyId: Long) {
        startActivity(ThreadActivity.getIntent(requireContext(), studyId))
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.userStudyUiState.collectLatest { uiState ->
                    if (uiState is Joined) homeAdapter.submitList(uiState.userStudies)
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.rvHome.adapter = homeAdapter
    }
}
