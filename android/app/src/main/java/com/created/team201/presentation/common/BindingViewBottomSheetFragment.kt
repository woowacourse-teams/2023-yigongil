package com.created.team201.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.created.team201.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BindingViewBottomSheetFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater) -> VB
) : BottomSheetDialogFragment() {
    private var _binding: VB? = null
    val binding get() = _binding ?: error(R.string.error_baseFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = bindingInflater(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
