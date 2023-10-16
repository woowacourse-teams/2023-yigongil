package com.created.team201.presentation.splash

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import com.created.team201.R
import com.created.team201.databinding.LayoutDialogInAppUpdateBinding
import com.created.team201.presentation.splash.model.AppUpdateType
import com.created.team201.presentation.splash.model.AppUpdateType.FLEXIBLE
import com.created.team201.presentation.splash.model.AppUpdateType.IMMEDIATE

class InAppUpdateDialog(
    context: Context,
    private val appUpdateType: AppUpdateType,
    private val title: String,
    private val content: String,
    private val onUpdateClick: () -> Unit,
    private val onCancelClick: () -> Unit = { },
) : AppCompatDialog(context) {
    private val binding: LayoutDialogInAppUpdateBinding by lazy {
        LayoutDialogInAppUpdateBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initDialog()
        setOkClickListener()
        setCancelClickListener()
    }

    private fun initDialog() {
        with(this) {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
        setCancelVisible(appUpdateType)
        binding.tvDialogInAppUpdateTitle.text = title
        binding.tvDialogInAppUpdateContent.text =
            content.ifBlank { context.getString(R.string.in_app_update_dialog_content) }
    }

    private fun setCancelVisible(appUpdateType: AppUpdateType) {
        when (appUpdateType) {
            IMMEDIATE -> binding.tvDialogInAppUpdateBtnCancel.visibility = View.GONE
            FLEXIBLE -> binding.tvDialogInAppUpdateBtnCancel.visibility = View.VISIBLE
        }
    }

    private fun setOkClickListener() {
        binding.tvDialogInAppUpdateBtnOk.setOnClickListener {
            onUpdateClick.invoke()
            dismiss()
        }
    }

    private fun setCancelClickListener() {
        binding.tvDialogInAppUpdateBtnCancel.setOnClickListener {
            onCancelClick.invoke()
            dismiss()
        }
    }
}
