package com.created.team201.presentation.studyList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ItemStudyListBinding

class StudyListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_study_list, parent, false),
) {

    private val binding = ItemStudyListBinding.bind(itemView)
    private lateinit var studySummary: StudySummaryUiModel

    fun bind(item: StudySummaryUiModel) {
        studySummary = item
        binding.studySummary = studySummary
    }
}
