package com.created.team201.presentation.studyList

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyListBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.guest.bottomSheet.LoginBottomSheetFragment
import com.created.team201.presentation.main.MainViewModel
import com.created.team201.presentation.studyDetail.StudyDetailActivity
import com.created.team201.presentation.studyList.adapter.StudyListAdapter
import com.created.team201.presentation.studyList.model.StudyStatus
import com.created.team201.presentation.updateStudy.UpdateStudyActivity
import com.created.team201.util.FirebaseLogUtil
import com.created.team201.util.FirebaseLogUtil.SCREEN_STUDY_LIST
import kotlinx.coroutines.launch

class StudyListFragment : BindingFragment<FragmentStudyListBinding>(R.layout.fragment_study_list) {

    private val studyListViewModel: StudyListViewModel by activityViewModels {
        StudyListViewModel.Factory
    }

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.Factory
    }

    private val studyListAdapter: StudyListAdapter by lazy {
        StudyListAdapter(studyListClickListener())
    }
    private var searchWord = ""

    override fun onResume() {
        super.onResume()

        FirebaseLogUtil.logScreenEvent(
            SCREEN_STUDY_LIST,
            this@StudyListFragment.javaClass.simpleName,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupStudyListSettings()
        setupDataObserve()
        setupRefreshListener()
        setupCreateStudyListener()
        setupScrollListener()
        setupStudyList()
        setupStudyListFilter()
    }

    private fun setupToolbar() {
        val menu = binding.tbStudyList.menu
        val searchItem = menu.findItem(R.id.menu_study_list_search)
        val searchView = searchItem.actionView as SearchView

        searchView.isIconified = false
        searchView.isFocusable = true

        setOnSearchViewQueryTextFocusChangeListener(searchView)
        setOnSearchItemCloseListener(searchView, searchItem)
        setOnSearchViewExpandListener(searchItem, searchView)
        setOnSearchViewQueryTextListener(searchView)
    }

    private fun setOnSearchViewQueryTextFocusChangeListener(searchView: SearchView) {
        searchView.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view?.postDelayed({
                    view.requestFocus()
                    val inputMethodManager: InputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(
                        view.findFocus(),
                        InputMethodManager.SHOW_IMPLICIT,
                    )
                }, 30)
            }
        }
    }

    private fun setOnSearchItemCloseListener(searchView: SearchView, searchItem: MenuItem) {
        searchView.setOnCloseListener {
            searchItem.collapseActionView()
            studyListViewModel.changeSearchMode(false)
            true
        }
    }

    private fun setOnSearchViewExpandListener(searchItem: MenuItem, searchView: SearchView) {
        searchItem
            .setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    studyListViewModel.changeSearchMode(true)
                    searchView.requestFocus()
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    searchView.hideKeyboard()
                    studyListViewModel.changeSearchMode(false)
                    studyListViewModel.refreshPage()
                    return true
                }
            })
    }

    private fun setOnSearchViewQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                studyListViewModel.changeSearchMode(true)
                searchWord = query.toString()
                studyListViewModel.loadSearchedPage(searchWord)
                searchView.hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun setupStudyListSettings() {
        binding.rvStudyListList.apply {
            adapter = studyListAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupDataObserve() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.studyListViewModel = studyListViewModel
        studyListViewModel.studySummaries.observe(viewLifecycleOwner) {
            studyListAdapter.submitList(it)
        }
    }

    private fun setupRefreshListener() {
        binding.srlStudyList.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                studyListViewModel.refreshPage()
                binding.srlStudyList.isRefreshing = false
            }
        }
    }

    private fun setupCreateStudyListener() {
        binding.fabStudyListCreateButton.setOnClickListener {
            if (mainViewModel.isGuest) {
                showLoginBottomSheetDialog()
                return@setOnClickListener
            }

            startActivity(
                UpdateStudyActivity.getIntent(
                    context = requireContext(),
                    viewMode = UpdateStudyActivity.CREATE_MODE,
                    studyId = null,
                ),
            )
        }
    }

    private fun showLoginBottomSheetDialog() {
        removeAllFragment()
        LoginBottomSheetFragment().show(
            childFragmentManager,
            LoginBottomSheetFragment.TAG_LOGIN_BOTTOM_SHEET,
        )
    }

    private fun removeAllFragment() {
        childFragmentManager.fragments.forEach {
            childFragmentManager.commit {
                remove(it)
            }
        }
    }

    private fun setupScrollListener() {
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                studyListViewModel.updateScrollState(newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var totalItemHeight = 0
                var recyclerViewHeight = 0
                val layoutManager = binding.rvStudyListList.layoutManager
                val adapter = binding.rvStudyListList.adapter

                if (adapter != null && layoutManager != null) {
                    for (i in 0 until adapter.itemCount) {
                        val itemView = layoutManager.findViewByPosition(i)
                        itemView?.let {
                            totalItemHeight += it.height
                        }
                    }

                    recyclerViewHeight = recyclerView.height
                }

                if (binding.srlStudyList.isRefreshing ||
                    binding.pbStudyListLoad.visibility == VISIBLE ||
                    totalItemHeight < recyclerViewHeight
                ) {
                    return
                }

                if (!binding.rvStudyListList.canScrollVertically(1)) {
                    studyListViewModel.loadNextPage(searchWord)
                }
            }
        }
        binding.rvStudyListList.addOnScrollListener(onScrollListener)
    }

    private fun setupStudyList() {
        studyListViewModel.initPage()
    }

    private fun studyListClickListener() = object : StudyListClickListener {
        override fun onClickStudySummary(id: Long) {
            startActivity(StudyDetailActivity.getIntent(requireContext(), id))
            activity?.overridePendingTransition(R.anim.right_in, R.anim.stay)
        }
    }

    private fun setupStudyListFilter() {
        binding.cgStudyList.setOnCheckedStateChangeListener { group, _ ->
            studyListViewModel.studySummaries.value?.let { studyList ->
                when (group.checkedChipId) {
                    binding.chipStudyListWaiting.id -> {
                        studyListAdapter.submitList(studyList.filter { study ->
                            study.processingStatus == StudyStatus.WAITING
                        })
                        return@let
                    }

                    binding.chipStudyListProcessing.id -> {
                        studyListAdapter.submitList(studyList.filter { study ->
                            study.processingStatus == StudyStatus.PROCESSING
                        })
                        return@let
                    }

                    binding.chipStudyListRecruiting.id -> {
                        studyListAdapter.submitList(studyList.filter { study ->
                            study.processingStatus == StudyStatus.RECRUITING
                        })
                        return@let
                    }

                    binding.chipStudyListEnd.id -> {
                        studyListAdapter.submitList(studyList.filter { study ->
                            study.processingStatus == StudyStatus.END
                        })
                        return@let
                    }
                }
                studyListAdapter.submitList(studyList)
            }
        }
    }

    private fun View.hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
