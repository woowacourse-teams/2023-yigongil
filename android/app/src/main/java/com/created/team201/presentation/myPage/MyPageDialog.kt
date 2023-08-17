package com.created.team201.presentation.myPage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.created.team201.databinding.LayoutDialogMyPageBinding

class MyPageDialog(
    private val title: String,
    private val content: String,
    private val myPageDialogClickListener: MyPageDialogClickListener,
) : DialogFragment() {
    private var _binding: LayoutDialogMyPageBinding? = null
    private val binding: LayoutDialogMyPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LayoutDialogMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()
        setCancelClickListener()
        setOkClickListener()
    }

    private fun initDialog() {
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setCanceledOnTouchOutside(false)
        }
        binding.tvDialogSettingTitle.text = title
        binding.tvDialogSettingContent.text = content
    }

    private fun setCancelClickListener() {
        binding.tvDialogSettingBtnCancel.setOnClickListener {
            myPageDialogClickListener.onCancelClick()
            dismissNow()
        }
    }

    private fun setOkClickListener() {
        binding.tvDialogSettingBtnOk.setOnClickListener {
            myPageDialogClickListener.onOkClick()
            dismissNow()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
