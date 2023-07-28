package com.created.team201.presentation.studyManagement.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyManagement.StudyMemberClickListener
import com.created.team201.presentation.studyManagement.model.StudyMemberUiModel

class StudyManagementMemberAdapter(
    private val memberClickListener: StudyMemberClickListener,
) :
    ListAdapter<StudyMemberUiModel, StudyManagementMemberViewHolder>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StudyManagementMemberViewHolder {
        return StudyManagementMemberViewHolder(
            StudyManagementMemberViewHolder.getBinding(parent),
            memberClickListener,
        )
    }

    override fun onBindViewHolder(holder: StudyManagementMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StudyMemberUiModel>() {
            override fun areItemsTheSame(
                oldItem: StudyMemberUiModel,
                newItem: StudyMemberUiModel,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: StudyMemberUiModel,
                newItem: StudyMemberUiModel,
            ): Boolean = oldItem == newItem
        }
    }
}
