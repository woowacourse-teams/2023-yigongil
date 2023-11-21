package com.created.team201.presentation.createStudy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.created.team201.R
import com.created.team201.databinding.FragmentFirstCreateStudyBinding
import com.created.team201.presentation.common.BindingViewFragment
import com.created.team201.presentation.createStudy.bottomSheet.CycleBottomSheetFragment
import com.created.team201.presentation.createStudy.bottomSheet.PeopleCountBottomSheetFragment
import com.created.team201.presentation.createStudy.bottomSheet.StudyDateBottomSheetFragment
import com.created.team201.util.collectOnStarted
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FirstCreateStudyFragment :
    BindingViewFragment<FragmentFirstCreateStudyBinding>(FragmentFirstCreateStudyBinding::inflate) {
    private val createStudyViewModel: CreateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPeopleCount()
        setupStudyDate()
        setupStudyCycle()
        setupNextClick()
        setupCollectEnableNext()
    }

    private fun setupPeopleCount() {
        createStudyViewModel.peopleCount.collectOnStarted(viewLifecycleOwner) { peopleCount ->
            if (peopleCount == -1) return@collectOnStarted

            binding.tvFirstCreateStudyPeopleCount.text =
                getString(
                    R.string.first_create_study_formatter_information_people_count,
                    peopleCount
                )
        }

        binding.tvFirstCreateStudyPeopleCount.setOnClickListener(
            setOnInputClick(TAG_CREATE_STUDY_PEOPLE_COUNT)
        )
    }

    private fun setupStudyDate() {
        createStudyViewModel.studyDate.collectOnStarted(viewLifecycleOwner) { studyDate ->
            if (studyDate == -1) return@collectOnStarted

            binding.tvFirstCreateStudyStudyDate.text =
                getString(R.string.first_create_study_formatter_information_study_date, studyDate)
        }

        binding.tvFirstCreateStudyStudyDate.setOnClickListener(
            setOnInputClick(TAG_CREATE_STUDY_STUDY_DATE)
        )
    }

    private fun setupStudyCycle() {
        createStudyViewModel.cycle.collectOnStarted(viewLifecycleOwner) { cycle ->
            if (cycle == -1) return@collectOnStarted

            binding.tvFirstCreateStudyCycle.text =
                getString(R.string.first_create_study_formatter_information_cycle, cycle)
        }

        binding.tvFirstCreateStudyCycle.setOnClickListener(
            setOnInputClick(TAG_CREATE_STUDY_CYCLE)
        )
    }

    private fun setupNextClick() {
        binding.tvFirstCreateStudyBtnNext.setOnClickListener {
            createStudyViewModel.navigateToNext()
        }
    }

    private fun setOnInputClick(tag: String): View.OnClickListener = View.OnClickListener {
        removeAllFragment()
        createBottomSheetFragment(tag)?.show(childFragmentManager, tag)
    }

    private fun removeAllFragment() {
        childFragmentManager.fragments.forEach {
            childFragmentManager.commit {
                remove(it)
            }
        }
    }

    private fun createBottomSheetFragment(tag: String): BottomSheetDialogFragment? {
        return when (tag) {
            TAG_CREATE_STUDY_PEOPLE_COUNT -> PeopleCountBottomSheetFragment()
            TAG_CREATE_STUDY_STUDY_DATE -> StudyDateBottomSheetFragment()
            TAG_CREATE_STUDY_CYCLE -> CycleBottomSheetFragment()
            else -> null
        }
    }

    private fun setupCollectEnableNext() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createStudyViewModel.isEnableFirstCreateStudyNext.collect { isEnable ->
                    binding.tvFirstCreateStudyBtnNext.isEnabled = isEnable
                }
            }
        }
    }

    companion object {
        private const val TAG_CREATE_STUDY_PEOPLE_COUNT = "TAG_CREATE_STUDY_PEOPLE_COUNT"
        private const val TAG_CREATE_STUDY_STUDY_DATE = "TAG_CREATE_STUDY_STUDY_DATE"
        private const val TAG_CREATE_STUDY_CYCLE = "TAG_CREATE_STUDY_CYCLE"
    }
}
