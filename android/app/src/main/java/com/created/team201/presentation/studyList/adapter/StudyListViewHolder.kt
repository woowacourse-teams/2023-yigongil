package com.created.team201.presentation.studyList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ItemStudyListBinding
import com.created.team201.presentation.studyList.StudyListClickListener
import com.created.team201.presentation.studyList.model.StudySummaryUiModel

class StudyListViewHolder(
    private val parent: ViewGroup,
    studyListClickListener: StudyListClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_study_list, parent, false),
) {

    private val binding = ItemStudyListBinding.bind(itemView)
    private lateinit var studySummary: StudySummaryUiModel

    init {
        itemView.setOnClickListener {
            studyListClickListener.onClickStudySummary(itemId)
        }
    }

    fun bind(item: StudySummaryUiModel) {
        studySummary = item
        binding.studySummary = studySummary
        binding.periodStringFormat =
            parent.context.resources.getStringArray(R.array.periodUnitStrings).toList()
        binding.startDateStringFormat =
            parent.context.resources.getStringArray(R.array.startDateStrings).toList()
    }
}
