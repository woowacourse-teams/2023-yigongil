package com.created.team201.presentation.studyManagement.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.common.TodoClickListener
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel

class StudyManagementAdapter(todoClickListener: TodoClickListener) :
    ListAdapter<StudyRoundDetailUiModel, StudyManagementViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyManagementViewHolder {
        return StudyManagementViewHolder(StudyManagementViewHolder.getBinding(parent))
    }

    override fun onBindViewHolder(holder: StudyManagementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StudyRoundDetailUiModel>() {
            override fun areItemsTheSame(
                oldItem: StudyRoundDetailUiModel,
                newItem: StudyRoundDetailUiModel,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: StudyRoundDetailUiModel,
                newItem: StudyRoundDetailUiModel,
            ): Boolean = oldItem == newItem
        }
    }
}
