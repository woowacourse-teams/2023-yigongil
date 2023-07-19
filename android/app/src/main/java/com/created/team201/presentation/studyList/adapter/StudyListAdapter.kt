package com.created.team201.presentation.studyList.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyList.StudyListClickListener
import com.created.team201.presentation.studyList.model.StudySummaryUiModel

class StudyListAdapter(
    private val studyListClickListener: StudyListClickListener,
) : ListAdapter<StudySummaryUiModel, StudyListViewHolder>(StudyListDiffUtil) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long =
        getItem(position).id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyListViewHolder {
        return StudyListViewHolder(parent, studyListClickListener)
    }

    override fun onBindViewHolder(holder: StudyListViewHolder, position: Int) {
        holder.bind(currentList[position])
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