package com.created.team201.presentation.studyManage.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ItemStudyManageBinding
import com.created.team201.presentation.studyList.StudyListClickListener
import com.created.team201.presentation.studyList.adapter.StudyListAdapter
import com.created.team201.presentation.studyManage.model.OnGoingStudiesUiModel

class StudyManageViewHolder(
    parent: ViewGroup,
    studyListClickListener: StudyListClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_study_manage, parent, false),
) {
    private val binding = ItemStudyManageBinding.bind(itemView)
    private val studyListAdapter: StudyListAdapter by lazy {
        StudyListAdapter(studyListClickListener)
    }

    init {
        binding.rvStudyManageList.adapter = studyListAdapter
    }

    fun bind(item: OnGoingStudiesUiModel) {
        Log.d("hello", item.studySummariesUiModel.toString())
        studyListAdapter.submitList(item.studySummariesUiModel)
    }
}
