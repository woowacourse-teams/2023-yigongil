package com.created.team201.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.created.team201.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BindingBottomSheetFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
) : BottomSheetDialogFragment() {
    private var _binding: B? = null
    val binding get() = _binding ?: error(R.string.error_baseFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}