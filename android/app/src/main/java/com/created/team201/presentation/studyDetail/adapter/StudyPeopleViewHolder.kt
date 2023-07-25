package com.created.team201.presentation.studyDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemStudyDetailStudyPeopleBinding
import com.created.team201.presentation.studyDetail.model.StudyMember

class StudyPeopleViewHolder(val binding: ItemStudyDetailStudyPeopleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(studyMember: StudyMember) {
        binding.studyMember = studyMember
    }

    companion object {
        fun getBinding(parent: ViewGroup): ItemStudyDetailStudyPeopleBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemStudyDetailStudyPeopleBinding.inflate(inflater, parent, false)
        }
    }
}
