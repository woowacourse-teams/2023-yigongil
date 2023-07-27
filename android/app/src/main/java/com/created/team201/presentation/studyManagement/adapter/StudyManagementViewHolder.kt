package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemStudyManagementBinding
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel

class StudyManagementViewHolder(val binding: ItemStudyManagementBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(studyManagementUIModel: StudyRoundDetailUiModel) {
        binding.studyManagement = studyManagementUIModel
    }

    companion object {
        fun getBinding(parent: ViewGroup): ItemStudyManagementBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemStudyManagementBinding.inflate(inflater, parent, false)
        }
    }
}
