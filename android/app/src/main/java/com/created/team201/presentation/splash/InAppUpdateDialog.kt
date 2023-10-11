package com.created.team201.presentation.splash

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.created.team201.databinding.LayoutDialogInAppUpdateBinding

class InAppUpdateDialog(
    context: Context,
    private val title: String,
    private val content: String,
    private val onUpdateClick: () -> Unit,
) : AppCompatDialog(context) {
    private val binding: LayoutDialogInAppUpdateBinding by lazy {
        LayoutDialogInAppUpdateBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initDialog()
        setOkClickListener()
    }

    private fun initDialog() {
        with(this) {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
        binding.tvDialogInAppUpdateTitle.text = title
        binding.tvDialogInAppUpdateContent.text = content
    }

    private fun setOkClickListener() {
        binding.tvDialogInAppUpdateBtnOk.setOnClickListener {
            onUpdateClick.invoke()
            dismiss()
        }
    }
}
