package com.created.team201.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.created.team201.R
import com.created.team201.databinding.FragmentHome2Binding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.home.adapter.HomeAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment2 : BindingFragment<FragmentHome2Binding>(R.layout.fragment_home2) {
    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter() }
    private val homeViewModel2: HomeFragment2ViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        collectWithLifecycle(homeViewModel2.homeViewState) {
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