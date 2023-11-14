package com.created.team201.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.created.team201.data.model.UserStudyEntity
import com.created.team201.databinding.FragmentHomeBinding
import com.created.team201.presentation.common.BindingViewFragment
import com.created.team201.presentation.home.adapter.HomeAdapter
import com.created.team201.presentation.studyThread.ThreadActivity
import com.created.team201.util.collectOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingViewFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter(::navigateToThreadActivity) }
    private val homeViewModel: HomeViewModel by viewModels()

    private fun navigateToThreadActivity(studyId: Long) {
        startActivity(ThreadActivity.getIntent(requireContext(), studyId))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        collectUiState()
    }

    private fun setupAdapter() {
        binding.rvHome.adapter = homeAdapter
        binding.rvHome.setHasFixedSize(true)
    }

    private fun collectUiState() {
        homeViewModel.uiState.collectOnStarted(this) { uiState ->
            when (uiState) {
                is HomeUiState.Success -> setUpView(uiState.userStudies)
                is HomeUiState.Loading -> {}
                is HomeUiState.Failed -> {}
            }
        }
    }

    private fun setUpView(userStudies: List<UserStudyEntity>) {
        when (userStudies.isEmpty()) {
            true -> binding.tvHomeNoStudy.visibility = View.VISIBLE
            false -> {
                binding.tvHomeNoStudy.visibility = View.INVISIBLE
                homeAdapter.submitList(userStudies)
            }
        }
    }
}
