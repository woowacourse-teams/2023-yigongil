package com.created.team201.presentation.studyList

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyListBinding
import com.created.team201.presentation.common.BindingFragment

class StudyListFragment : BindingFragment<FragmentStudyListBinding>(R.layout.fragment_study_list) {

    private val studyListViewModel: StudyListViewModel by viewModels()
    private lateinit var studyListAdapter: StudyListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpAdapter()
        setUpScrollListener()
        setUpStudyListObserve()
    }

    private fun setUpToolbar() {
        binding.tbStudyList.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_study_list_search -> {
                    // 스터디 검색 뷰로 이동
                    true
                }

                else -> false
            }
        }
    }

    private fun setUpAdapter() {
        studyListAdapter = StudyListAdapter()
        binding.rvStudyListList.adapter = studyListAdapter
    }

    private fun setUpScrollListener() {
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.fabStudyListCreateButton.visibility = VISIBLE
                } else {
                    binding.fabStudyListCreateButton.visibility = INVISIBLE
                }
            }
        }
        binding.rvStudyListList.addOnScrollListener(onScrollListener)
    }

    private fun setUpStudyListObserve() {
        studyListViewModel.studySummaries.observe(viewLifecycleOwner) {
            studyListAdapter.submitList(it)
        }
    }
}
