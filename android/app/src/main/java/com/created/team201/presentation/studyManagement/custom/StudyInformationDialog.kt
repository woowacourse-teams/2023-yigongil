package com.created.team201.presentation.studyManagement.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.created.team201.R
import com.created.team201.databinding.DialogStudyInformaionBinding
import com.created.team201.presentation.studyManagement.model.StudyManagementInformationUiModel

class StudyInformationDialog(
    context: Context,
    private val studyInformation: StudyManagementInformationUiModel,
) : Dialog(context) {
    private var _binding: DialogStudyInformaionBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DialogStudyInformaionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0.8f)
        binding.studyInformation = studyInformation
        binding.periodStringFormat =
            context.resources.getStringArray(R.array.periodUnitStrings).toList()
    }
}
