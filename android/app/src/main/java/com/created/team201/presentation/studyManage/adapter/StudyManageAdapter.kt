package com.created.team201.presentation.studyManage.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyList.StudyListClickListener
import com.created.team201.presentation.studyManage.model.OnGoingStudiesUiModel

class StudyManageAdapter(
    private val studyListClickListener: StudyListClickListener,
) : ListAdapter<OnGoingStudiesUiModel, StudyManageViewHolder>(StudyManageDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyManageViewHolder {
        return StudyManageViewHolder(parent, studyListClickListener)
    }

    override fun onBindViewHolder(holder: StudyManageViewHolder, position: Int) {
        Log.d("hello", getItem(position).studySummariesUiModel.toString())
        holder.bind(getItem(position))
    }

    companion object {
        private val StudyManageDiffUtil = object : DiffUtil.ItemCallback<OnGoingStudiesUiModel>() {
            override fun areItemsTheSame(
                oldItem: OnGoingStudiesUiModel,
                newItem: OnGoingStudiesUiModel,
            ): Boolean =
                oldItem.status == newItem.status

            override fun areContentsTheSame(
                oldItem: OnGoingStudiesUiModel,
                newItem: OnGoingStudiesUiModel,
            ): Boolean =
                oldItem == newItem
        }
    }
}
