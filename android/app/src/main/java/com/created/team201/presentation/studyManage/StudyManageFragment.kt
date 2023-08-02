package com.created.team201.presentation.studyManage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyManageBinding
import com.created.team201.presentation.common.BindingFragment
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

    private fun setUpViewPagerAdapter() {
        binding.vpStudyManage.adapter = studyManageAdapter
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
            // 스터디 상세보기 뷰로 이동
            // 참여한인지, 개설한인지 구분
            startActivity(StudyManagementActivity.getIntent(requireContext(), id, 1))
        }
    }
}
