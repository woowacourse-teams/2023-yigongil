package com.created.team201.presentation.studyList

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class StudyListAdapter : ListAdapter<StudySummaryUiModel, StudyListViewHolder>(StudyListDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyListViewHolder {
        return StudyListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: StudyListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val StudyListDiffUtil = object : DiffUtil.ItemCallback<StudySummaryUiModel>() {
            override fun areItemsTheSame(
                oldItem: StudySummaryUiModel,
                newItem: StudySummaryUiModel,
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: StudySummaryUiModel,
                newItem: StudySummaryUiModel,
            ): Boolean =
                oldItem == newItem
        }
    }
}
