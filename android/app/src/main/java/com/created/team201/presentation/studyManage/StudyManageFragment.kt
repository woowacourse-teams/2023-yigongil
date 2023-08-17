package com.created.team201.presentation.studyManage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.OVER_SCROLL_NEVER
import androidx.viewpager2.widget.ViewPager2
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyManageBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.studyDetail.StudyDetailActivity
import com.created.team201.presentation.studyList.StudyListClickListener
import com.created.team201.presentation.studyManage.adapter.StudyManageAdapter
import com.created.team201.presentation.studyManage.model.MyStudyStatus.OPENED
import com.created.team201.presentation.studyManage.model.MyStudyStatus.PARTICIPATED
import com.created.team201.presentation.studyManagement.StudyManagementActivity
import com.created.team201.util.FirebaseLogUtil
import com.created.team201.util.FirebaseLogUtil.SCREEN_STUDY_MANAGE
import com.created.team201.util.FirebaseLogUtil.SCREEN_STUDY_MANAGE_OPENED
import com.created.team201.util.FirebaseLogUtil.SCREEN_STUDY_MANAGE_PARTICIPATED
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class StudyManageFragment :
    BindingFragment<FragmentStudyManageBinding>(R.layout.fragment_study_manage) {

    private val studyManageViewModel: StudyManageViewModel by viewModels {
        StudyManageViewModel.Factory
    }
    private val studyManageAdapter: StudyManageAdapter by lazy {
        StudyManageAdapter(studyListClickListener())
    }

    override fun onResume() {
        super.onResume()

        FirebaseLogUtil.logScreenEvent(
            SCREEN_STUDY_MANAGE,
            this@StudyManageFragment.javaClass.simpleName
        )

        studyManageViewModel.loadStudies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPagerAdapter()
        setUpTabLayoutViewPagerConnection()
        setUpStudyManageObserve()
        setUpRefreshListener()
        setupTabChanged()
    }

    private fun setUpRefreshListener() {
        binding.srlStudyManage.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                studyManageViewModel.loadStudies()
                binding.srlStudyManage.isRefreshing = false
            }
        }
    }

    private fun setUpViewPagerAdapter() {
        binding.vpStudyManage.apply {
            adapter = studyManageAdapter
            getChildAt(PAGE_INDEX_ZERO).overScrollMode = OVER_SCROLL_NEVER
        }
    }

    private fun setUpTabLayoutViewPagerConnection() {
        val tabTitles = resources.getStringArray(R.array.tabLayoutTitles)
        TabLayoutMediator(binding.tlStudyManage, binding.vpStudyManage) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun setUpStudyManageObserve() {
        studyManageViewModel.myStudiesUiModel.observe(viewLifecycleOwner) {
            studyManageAdapter.submitList(it)
        }
    }

    private fun setupTabChanged() {
        binding.vpStudyManage.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    studyManageViewModel.myStudiesUiModel.value?.let {
                        when (it[position].status) {
                            PARTICIPATED -> {
                                FirebaseLogUtil.logScreenEvent(
                                    SCREEN_STUDY_MANAGE_PARTICIPATED,
                                    this@StudyManageFragment.javaClass.simpleName
                                )
                            }

                            OPENED -> {
                                FirebaseLogUtil.logScreenEvent(
                                    SCREEN_STUDY_MANAGE_OPENED,
                                    this@StudyManageFragment.javaClass.simpleName
                                )
                            }
                        }
                    }
                }
            },
        )
    }

    private fun studyListClickListener() = object : StudyListClickListener {
        override fun onClickStudySummary(id: Long) {
            when (studyManageViewModel.getMyStudyInProcessing(id)) {
                true -> startActivity(
                    StudyManagementActivity.getIntent(
                        requireContext(),
                        id,
                    ),
                )

                false -> startActivity(StudyDetailActivity.getIntent(requireContext(), id))
            }
        }
    }

    companion object {
        private const val PAGE_INDEX_ZERO = 0
    }
}
