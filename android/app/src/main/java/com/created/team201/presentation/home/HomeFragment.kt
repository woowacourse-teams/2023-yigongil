package com.created.team201.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentHomeBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.home.adapter.DashboardAdapter
import com.created.team201.presentation.home.model.TodoUiModel
import com.created.team201.presentation.studyManagement.StudyManagementActivity

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    private val dashboardAdapter: DashboardAdapter by lazy {
        DashboardAdapter(implementClickListener())
    }
    private val customViewPager: CustomViewPager by lazy {
        CustomViewPager(binding, requireContext())
    }

    private fun implementClickListener() = object : HomeClickListener {

        override fun clickOnNecessaryTodoCheck(todo: TodoUiModel, roundId: Int, isDone: Boolean) {
            homeViewModel.updateNecessaryTodo(todo, roundId, !isDone)
        }

        override fun clickOnOptionalTodoCheck(todo: TodoUiModel, roundId: Int, isDone: Boolean) {
            homeViewModel.updateOptionalTodo(todo, roundId, !isDone)
        }

        override fun clickOnStudyCard(studyId: Long) {
            navigateToStudyDetailActivity(studyId)
        }
    }

    private fun navigateToStudyDetailActivity(studyId: Long) {
        startActivity(StudyManagementActivity.getIntent(requireContext(), studyId))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()
        initAdapter()
        observeUserStudies()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.updateUserStudies()
    }

    private fun bindViewModel() {
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initAdapter() {
        customViewPager.setCustomViewPager()
        binding.vpHome.adapter = dashboardAdapter
    }

    private fun observeUserStudies() {
        homeViewModel.userStudies.observe(viewLifecycleOwner) { studyList ->
            dashboardAdapter.submitList(studyList)
        }
    }
}
