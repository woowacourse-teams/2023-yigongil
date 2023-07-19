package com.created.team201.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentHomeBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.home.adapter.DashboardAdapter
import com.created.team201.presentation.studyDetail.StudyDetailActivity

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private val dashboardAdapter: DashboardAdapter by lazy {
        DashboardAdapter(implementClickListener())
    }
    private val customViewPager: CustomViewPager by lazy {
        CustomViewPager(binding, requireContext())
    }

    private fun implementClickListener() = object : HomeClickListener {
        override fun clickOnTodo(id: Int, isDone: Boolean) {
            homeViewModel.patchTodo(id, !isDone)
        }

        override fun clickOnStudyCard() {
            navigateToStartDetail()
        }
    }

    private fun navigateToStartDetail() {
        startActivity(StudyDetailActivity.getIntent(requireContext(), 0))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()
        initAdapter()
        observeUserStudies()

        // 뷰모델 팩토리 어떻게 할 것 인지

        homeViewModel.getUserStudyInfo()
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
