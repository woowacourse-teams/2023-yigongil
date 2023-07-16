package com.created.team201.presentation.studyList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ItemStudyListBinding
import com.created.team201.presentation.studyList.model.StudySummaryUiModel

class StudyListViewHolder(private val parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_study_list, parent, false),
) {

    private val binding = ItemStudyListBinding.bind(itemView)
    private lateinit var studySummary: StudySummaryUiModel

    fun bind(item: StudySummaryUiModel, resId: Int) {
        studySummary = item
        binding.studySummary = studySummary
        binding.clStudyList.background = getDrawable(parent.context, resId)
    }
}
