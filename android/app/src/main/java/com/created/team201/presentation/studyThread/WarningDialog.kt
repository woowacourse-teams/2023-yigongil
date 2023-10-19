package com.created.team201.presentation.studyThread

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.created.team201.databinding.LayoutDialogThreadWarningBinding
import com.created.team201.presentation.studyThread.model.WarningType

class WarningDialog(
    context: Context,
    private val warningType: WarningType,
    private val onAcceptClick: () -> Unit,
    private val onCancelClick: () -> Unit = { },
) : AppCompatDialog(context) {
    private val binding: LayoutDialogThreadWarningBinding by lazy {
        LayoutDialogThreadWarningBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupDialog()
        binding.warningType = warningType
        setOkClickListener()
        setCancelClickListener()
    }

    private fun setupDialog() {
        with(this) {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
    }

    private fun setOkClickListener() {
        binding.tvThreadWarningDialogBtnOk.setOnClickListener {
            onAcceptClick()
            dismiss()
            ownerActivity?.finish()
        }
    }

    private fun setCancelClickListener() {
        binding.tvThreadWarningDialogBtnCancel.setOnClickListener {
            onCancelClick()
            dismiss()
        }
    }
}
