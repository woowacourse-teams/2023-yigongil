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
import com.created.team201.presentation.home.adapter.HomeAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter() }
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        collectWithLifecycle(homeViewModel.homeViewState) {
            homeAdapter.submitList(it)
        }
    }

    private fun setupAdapter() {
        binding.rvHome.adapter = homeAdapter
        binding.rvHome.setHasFixedSize(true)
    }

    private inline fun <T> collectWithLifecycle(
        flow: Flow<T>,
        crossinline action: (T) -> Unit,
    ) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest { value ->
                    action(value)
                }
            }
        }
    }
}


//package com.created.team201.presentation.home
//
//import android.os.Bundle
//import android.view.View
//import androidx.fragment.app.viewModels
//import com.created.team201.R
//import com.created.team201.databinding.FragmentHomeBinding
//import com.created.team201.presentation.common.BindingFragment
//import com.created.team201.presentation.home.adapter.DashboardAdapter
//import com.created.team201.presentation.home.model.TodoWithRoundIdUiModel
//import com.created.team201.presentation.studyManagement.StudyManagementActivity
//import com.created.team201.util.FirebaseLogUtil
//import com.created.team201.util.FirebaseLogUtil.SCREEN_HOME
//
//class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
//    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
//    private val dashboardAdapter: DashboardAdapter by lazy {
//        DashboardAdapter(implementClickListener())
//    }
//    private val customViewPager: CustomViewPager by lazy {
//        CustomViewPager(binding, requireContext())
//    }
//
//    private fun implementClickListener() = object : HomeClickListener {
//
//        override fun clickOnNecessaryTodoCheck(todo2: TodoWithRoundIdUiModel, isDone: Boolean) {
//            homeViewModel.updateNecessaryTodo(todo2, !isDone)
//        }
//
//        override fun clickOnOptionalTodoCheck(todo2: TodoWithRoundIdUiModel, isDone: Boolean) {
//            homeViewModel.updateOptionalTodo(todo2, !isDone)
//        }
//
//        override fun clickOnStudyCard(studyId: Long) {
//            navigateToStudyDetailActivity(studyId)
//        }
//    }
//
//    private fun navigateToStudyDetailActivity(studyId: Long) {
//        startActivity(StudyManagementActivity.getIntent(requireContext(), studyId))
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        bindViewModel()
//        initAdapter()
//        observeUserStudies()
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        FirebaseLogUtil.logScreenEvent(SCREEN_HOME, this@HomeFragment.javaClass.simpleName)
//
//        homeViewModel.updateUserStudies()
//    }
//
//    private fun bindViewModel() {
//        binding.viewModel = homeViewModel
//        binding.lifecycleOwner = viewLifecycleOwner
//    }
//
//    private fun initAdapter() {
//        customViewPager.setCustomViewPager()
//        binding.vpHome.adapter = dashboardAdapter
//    }
//
//    private fun observeUserStudies() {
//        homeViewModel.userStudies.observe(viewLifecycleOwner) { studyList ->
//            dashboardAdapter.submitList(studyList)
//        }
//    }
//}
