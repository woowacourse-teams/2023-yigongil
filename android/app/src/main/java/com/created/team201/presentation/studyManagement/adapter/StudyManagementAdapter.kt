package com.created.team201.presentation.studyManagement.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyManagement.StudyManagementClickListener
import com.created.team201.presentation.studyManagement.StudyMemberClickListener
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel

class StudyManagementAdapter(
    private val studyManagementClickListener: StudyManagementClickListener,
    private val studyMemberClickListener: StudyMemberClickListener,
) :
    ListAdapter<StudyRoundDetailUiModel, StudyManagementViewHolder>(diffCallback) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyManagementViewHolder {
        return StudyManagementViewHolder(
            StudyManagementViewHolder.getBinding(parent),
            studyManagementClickListener,
            studyMemberClickListener,
        )
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
