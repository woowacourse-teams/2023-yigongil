package com.created.team201.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentHomeBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.home.adapter.DashboardAdapter

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private val dashboardAdapter: DashboardAdapter by lazy { DashboardAdapter(implementClickListener()) }
    private val customViewPager: CustomViewPager by lazy {
        CustomViewPager(binding, requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initAdapter()
        observeUserStudies()

        homeViewModel.getUserStudyInfo()
        homeViewModel.userStudies.observe(viewLifecycleOwner) {
            Log.d("123123", it.toString())
        }

        binding.ivHomeLogo.setOnClickListener {
            homeViewModel.temp()
        }
    }

    private fun implementClickListener() = object : HomeClickListener {
        override fun clickOnTodo(id: Int, isDone: Boolean) {
            homeViewModel.update(id, isDone)
        }

        override fun clickOnStudyCard() {
            Log.d("123123", "123123stary")
        }
    }

    private fun initViewModel() {
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this
    }

    private fun initAdapter() {
        customViewPager.setCustomViewPager()
        binding.vpHome.adapter = dashboardAdapter
    }

    private fun observeUserStudies() {
        homeViewModel.userStudies.observe(viewLifecycleOwner) { homeUiModel ->
            dashboardAdapter.updateItems(homeUiModel)
        }
    }
}
