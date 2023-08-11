package com.created.team201.presentation.studyManage.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyList.StudyListClickListener
import com.created.team201.presentation.studyManage.model.MyStudiesUiModel

class StudyManageAdapter(
    private val studyListClickListener: StudyListClickListener,
) : ListAdapter<MyStudiesUiModel, StudyManageViewHolder>(StudyManageDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyManageViewHolder {
        return StudyManageViewHolder(parent, studyListClickListener)
    }

    override fun onBindViewHolder(holder: StudyManageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val StudyManageDiffUtil = object : DiffUtil.ItemCallback<MyStudiesUiModel>() {
            override fun areItemsTheSame(
                oldItem: MyStudiesUiModel,
                newItem: MyStudiesUiModel,
            ): Boolean =
                oldItem.status == newItem.status

            override fun areContentsTheSame(
                oldItem: MyStudiesUiModel,
                newItem: MyStudiesUiModel,
            ): Boolean =
                oldItem == newItem
        }
    }
}
