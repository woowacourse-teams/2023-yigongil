package com.created.team201.presentation.studyManage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView.OVER_SCROLL_NEVER
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyManageBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.studyDetail.StudyDetailActivity
import com.created.team201.presentation.studyList.StudyListClickListener
import com.created.team201.presentation.studyManage.adapter.StudyManageAdapter
import com.created.team201.presentation.studyManagement.StudyManagementActivity
import com.google.android.material.tabs.TabLayoutMediator

class StudyManageFragment :
    BindingFragment<FragmentStudyManageBinding>(R.layout.fragment_study_manage) {

    private val studyManageViewModel: StudyManageViewModel by viewModels {
        StudyManageViewModel.Factory
    }
    private val studyManageAdapter: StudyManageAdapter by lazy {
        StudyManageAdapter(studyListClickListener())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPagerAdapter()
        setUpTabLayoutViewPagerConnection()
        setUpStudyManageObserve()
    }

    override fun onResume() {
        super.onResume()

        studyManageViewModel.loadStudies()
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

    private fun studyListClickListener() = object : StudyListClickListener {
        override fun onClickStudySummary(id: Long) {
            when (studyManageViewModel.getMyStudyInProcessing(id)) {
                true -> startActivity(
                    StudyManagementActivity.getIntent(
                        requireContext(),
                        id,
                        studyManageViewModel.getMyRole(id).index,
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
