package com.created.team201.presentation.studyList

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout.VERTICAL
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyListBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.studyList.adapter.StudyListAdapter

class StudyListFragment : BindingFragment<FragmentStudyListBinding>(R.layout.fragment_study_list) {

    private val studyListViewModel: StudyListViewModel by viewModels()
    private val studyListAdapter: StudyListAdapter by lazy {
        StudyListAdapter(studyListClickListener())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpStudyListSettings()
        setUpStudyListObserve()
        setUpRefreshListener()
        setUpScrollListener()
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

    private fun setUpStudyListSettings() {
        val dividerItemDecoration = DividerItemDecoration(context, VERTICAL)
        getDrawable(requireContext(), R.drawable.divider_study_list)?.let {
            dividerItemDecoration.setDrawable(it)
        }

        binding.rvStudyListList.apply {
            adapter = studyListAdapter
            addItemDecoration(dividerItemDecoration)
            setHasFixedSize(true)
        }
    }

    private fun setUpStudyListObserve() {
        studyListViewModel.studySummaries.observe(viewLifecycleOwner) {
            studyListAdapter.submitList(it)
        }
    }

    private fun setUpRefreshListener() {
        binding.srlStudyList.setOnRefreshListener {
            // 새로 받아온 리스트 주입, 끝난 후 isRefreshing false로 변경
            // studyListAdapter.submitList()
            // binding.srlStudyList.isRefreshing = false
        }
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

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 데이터 호출 싱크 맞추기
                if (binding.srlStudyList.isRefreshing || binding.pbStudyListLoad.visibility == VISIBLE) {
                    return
                }

                if (!binding.rvStudyListList.canScrollVertically(1)) {
                    // while (loadPage) binding.pbStudyListLoad.visibility = VISIBLE
                    // after (loadPage) binding.pbStudyListLoad.visibility = GONE
                    studyListViewModel.loadPage()
                }
            }
        }
        binding.rvStudyListList.addOnScrollListener(onScrollListener)
    }

    private fun studyListClickListener() = object : StudyListClickListener {
        override fun onClickStudySummary(id: Long) {
            // 스터디 상세보기 뷰로 이동
        }
    }
}
