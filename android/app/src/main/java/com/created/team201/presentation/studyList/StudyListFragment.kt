package com.created.team201.presentation.studyList

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.LinearLayout.VERTICAL
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyListBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.createStudy.CreateStudyActivity
import com.created.team201.presentation.studyDetail.StudyDetailActivity
import com.created.team201.presentation.studyList.adapter.StudyListAdapter
import kotlinx.coroutines.launch

class StudyListFragment : BindingFragment<FragmentStudyListBinding>(R.layout.fragment_study_list) {

    private val studyListViewModel: StudyListViewModel by viewModels {
        StudyListViewModel.Factory
    }
    private val studyListAdapter: StudyListAdapter by lazy {
        StudyListAdapter(studyListClickListener())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpStudyListSettings()
        setUpDataObserve()
        setUpRefreshListener()
        setUpCreateStudyListener()
        setUpScrollListener()
    }

    override fun onResume() {
        super.onResume()

        studyListViewModel.refreshPage()
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

    private fun setUpDataObserve() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.studyListViewModel = studyListViewModel
        studyListViewModel.studySummaries.observe(viewLifecycleOwner) {
            studyListAdapter.submitList(it)
        }
    }

    private fun setUpRefreshListener() {
        binding.srlStudyList.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                studyListViewModel.refreshPage()
                binding.srlStudyList.isRefreshing = false
            }
        }
    }

    private fun setUpCreateStudyListener() {
        binding.fabStudyListCreateButton.setOnClickListener {
            startActivity(CreateStudyActivity.getIntent(requireContext()))
        }
    }

    private fun setUpScrollListener() {
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                studyListViewModel.updateScrollState(newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (binding.srlStudyList.isRefreshing || binding.pbStudyListLoad.visibility == VISIBLE) {
                    return
                }

                if (!binding.rvStudyListList.canScrollVertically(1)) {
                    studyListViewModel.loadNextPage()
                }
            }
        }
        binding.rvStudyListList.addOnScrollListener(onScrollListener)
    }

    private fun studyListClickListener() = object : StudyListClickListener {
        override fun onClickStudySummary(id: Long) {
            startActivity(StudyDetailActivity.getIntent(requireContext(), id))
        }
    }
}
