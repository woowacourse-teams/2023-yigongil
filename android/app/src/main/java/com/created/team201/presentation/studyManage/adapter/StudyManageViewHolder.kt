package com.created.team201.presentation.studyManage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout.GONE
import android.widget.LinearLayout.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ItemStudyManageBinding
import com.created.team201.presentation.studyList.StudyListClickListener
import com.created.team201.presentation.studyList.adapter.StudyListAdapter
import com.created.team201.presentation.studyManage.model.MyStudiesUiModel

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
        setUpStudyManageList()
    }

    private fun setUpStudyManageList() {
        binding.rvStudyManageList.apply {
            adapter = studyListAdapter
            setHasFixedSize(true)
        }
    }

    fun bind(item: MyStudiesUiModel) {
        studyListAdapter.submitList(item.studySummariesUiModel)
        when (item.studySummariesUiModel.isEmpty()) {
            true -> {
                binding.tvStudyManageNoStudy.visibility = VISIBLE
                binding.tvStudyManageNoStudy.setText(item.status.notice)
            }

            false -> binding.tvStudyManageNoStudy.visibility = GONE
        }
    }
}
