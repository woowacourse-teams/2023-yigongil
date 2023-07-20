package com.created.team201.presentation.studyDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemStudyDetailStudyPeopleBinding
import com.created.team201.presentation.studyDetail.model.StudyParticipant

class StudyPeopleViewHolder(val binding: ItemStudyDetailStudyPeopleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(studyParticipant: StudyParticipant) {
        binding.studyParticipant = studyParticipant
    }

    companion object {
        fun getBinding(parent: ViewGroup): ItemStudyDetailStudyPeopleBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemStudyDetailStudyPeopleBinding.inflate(inflater, parent, false)
        }
    }
}
