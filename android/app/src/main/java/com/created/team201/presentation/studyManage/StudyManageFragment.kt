package com.created.team201.presentation.studyManage

import android.os.Bundle
import android.view.View
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyManageBinding
import com.created.team201.presentation.common.BindingFragment

class StudyManageFragment :
    BindingFragment<FragmentStudyManageBinding>(R.layout.fragment_study_manage) {

    private val studyManageAdapter: StudyManageAdapter by lazy {
        StudyManageAdapter(
            studyListClickListener(),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPagerAdapter()
    }

    private fun setUpViewPagerAdapter() {
        binding.vpStudyManage.adapter = studyManageAdapter
    }

    private fun studyListClickListener() = object : StudyListClickListener {
        override fun onClickStudySummary(studySummary: StudySummaryUiModel) {
            // 스터디 상세보기 뷰로 이동
            // 참여한인지, 개설한인지 구분
        }
    }
}
