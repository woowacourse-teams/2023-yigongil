package com.created.team201.presentation.studyList

import android.os.Bundle
import android.view.View
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyListBinding
import com.created.team201.presentation.common.BindingFragment

class StudyListFragment : BindingFragment<FragmentStudyListBinding>(R.layout.fragment_study_list) {

    private lateinit var studyListAdapter: StudyListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()

    private fun setUpAdapter() {
        studyListAdapter = StudyListAdapter()
        binding.rvStudyListList.adapter = studyListAdapter
    }
    }
}
