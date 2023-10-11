package com.created.team201.presentation.splash

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.created.team201.databinding.LayoutDialogMyPageBinding

class InAppUpdateDialog(
    context: Context,
    private val title: String,
    private val content: String,
    private val inAppUpdateDialogClickListener: InAppUpdateDialogClickListener,
) : AppCompatDialog(context) {
    private val binding: LayoutDialogMyPageBinding by lazy {
        LayoutDialogMyPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initDialog()
        setCancelClickListener()
        setOkClickListener()
    }

    private fun initDialog() {
        with(this) {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
        binding.tvDialogSettingTitle.text = title
        binding.tvDialogSettingContent.text = content
    }

    private fun setCancelClickListener() {
        binding.tvDialogSettingBtnCancel.setOnClickListener {
            inAppUpdateDialogClickListener.onCancelClick()
            dismiss()
        }
    }

    private fun setOkClickListener() {
        binding.tvDialogSettingBtnOk.setOnClickListener {
            inAppUpdateDialogClickListener.onOkClick()
            dismiss()
        }
    }
}
