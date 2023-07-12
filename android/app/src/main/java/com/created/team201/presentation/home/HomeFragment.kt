package com.created.team201.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.created.team201.R
import com.created.team201.databinding.FragmentHomeBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.home.adapter.DashboardAdapter
import com.created.team201.util.ZoomOutPageTransformer

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private val dashboardAdapter: DashboardAdapter by lazy { DashboardAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initAdapter()

        homeViewModel.userStudies.observe(viewLifecycleOwner) {
            dashboardAdapter.updateItems(it)
        }
        homeViewModel.getUserStudyInfo()
    }

    private fun initViewModel() {
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this
    }

    private fun initAdapter() {
        with(binding.vpHome) {
            adapter = dashboardAdapter
            val displayWidth = requireContext().resources.displayMetrics.widthPixels
            val pageWidth = DISPLAY_RATIO * displayWidth
            val pagePadding = ((displayWidth - pageWidth) / 2).toInt()
            val innerPadding = pagePadding / 40
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            offscreenPageLimit = 1
            setPadding(pagePadding, 0, pagePadding, 58)
            setPageTransformer(
                CompositePageTransformer().apply {
                    addTransformer(ZoomOutPageTransformer())
                    addTransformer { page, position ->
                        page.translationX = position * -(innerPadding)
                    }
                },
            )
        }
    }

    companion object {
        private const val DEFAULT_WIDTH = 360.0
        private const val DISPLAY_RATIO = 256 / DEFAULT_WIDTH
    }
}
