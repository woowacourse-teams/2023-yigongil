package com.created.team201.presentation.updateStudy.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.created.team201.R
import com.created.team201.databinding.FragmentFirstUpdateStudyBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.updateStudy.UpdateStudyViewModel
import com.created.team201.presentation.updateStudy.bottomSheet.PeopleCountBottomSheetFragment
import com.created.team201.presentation.updateStudy.bottomSheet.StartDateBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstUpdateStudyFragment :
    BindingFragment<FragmentFirstUpdateStudyBinding>(R.layout.fragment_first_update_study) {
    private val updateStudyViewModel: UpdateStudyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = updateStudyViewModel
        binding.onInputClickListener = ::setOnInputClick
    }

    private fun setOnInputClick(tag: String) {
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
            getString(R.string.createStudy_tag_people_count) -> {
                PeopleCountBottomSheetFragment()
            }

            getString(R.string.createStudy_tag_start_date) -> {
                StartDateBottomSheetFragment()
            }

            else -> {
                null
            }
        }
    }
}
