package com.created.team201.presentation.studyThread

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.created.team201.databinding.DialogStudyInformaionBinding
import com.created.team201.presentation.studyDetail.model.StudyDetailUIModel

class StudyInformationDialog(
    context: Context,
    private val studyInformation: StudyDetailUIModel,
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
        binding.studyDetail = studyInformation
    }
}
