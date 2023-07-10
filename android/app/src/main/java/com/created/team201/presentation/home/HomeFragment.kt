package com.created.team201.presentation.home

import android.os.Bundle
import android.view.View
import com.created.team201.R
import com.created.team201.databinding.FragmentHomeBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.home.adapter.DashboardAdapter

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val dashboardAdapter: DashboardAdapter by lazy { DashboardAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpHome.adapter = dashboardAdapter
    }
}
