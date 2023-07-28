package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemStudyManagementBinding
import com.created.team201.presentation.studyManagement.StudyMemberClickListener
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel

class StudyManagementViewHolder(
    private val binding: ItemStudyManagementBinding,
    private val studyMemberClickListener: StudyMemberClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    private val studyManagementMemberAdapter: StudyManagementMemberAdapter by lazy {
        StudyManagementMemberAdapter(studyMemberClickListener)
    }

    init {
        binding.rvItemStudyManagementStudyMember.adapter = studyManagementMemberAdapter
    }

    fun bind(studyManagementUIModel: StudyRoundDetailUiModel) {
        binding.studyManagement = studyManagementUIModel
        studyManagementMemberAdapter.submitList(studyManagementUIModel.studyMembers)
    }

    companion object {
        fun getBinding(parent: ViewGroup): ItemStudyManagementBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemStudyManagementBinding.inflate(inflater, parent, false)
        }
    }
}
