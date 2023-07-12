package com.created.team201.presentation.createStudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentCreateStudyBottomSheetBinding
import com.created.team201.presentation.createStudy.custom.PickerChangeListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateStudyBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentCreateStudyBottomSheetBinding? = null
    private val binding get() = _binding ?: error(R.string.error_baseFragment)

    private val viewModel: CreateStudyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateStudyBottomSheetBinding.inflate(
            inflater,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        setChangeListener()
    }

    private fun setChangeListener() {
        binding.changeListener = object : PickerChangeListener {
            override fun onChange(value: Int) {
                viewModel.peopleCount.value = value
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
