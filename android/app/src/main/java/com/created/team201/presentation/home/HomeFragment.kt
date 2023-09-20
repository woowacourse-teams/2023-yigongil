package com.created.team201.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.created.team201.R
import com.created.team201.data.mapper.toUserStudyUiState
import com.created.team201.databinding.FragmentHomeBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.home.HomeViewModel.HomeUiState.FAIL
import com.created.team201.presentation.home.HomeViewModel.HomeUiState.IDLE
import com.created.team201.presentation.home.HomeViewModel.HomeUiState.SUCCESS
import com.created.team201.presentation.home.adapter.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter() }
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        collectUiState()
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collectLatest { uiState ->
                    when (uiState) {
                        is SUCCESS -> homeAdapter.submitList(uiState.homeStudies.toUserStudyUiState())
                        is FAIL -> {
                            // 에러처리
                        }

                        is IDLE -> throw IllegalStateException()
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.rvHome.adapter = homeAdapter
        binding.rvHome.setHasFixedSize(true)
    }
}
