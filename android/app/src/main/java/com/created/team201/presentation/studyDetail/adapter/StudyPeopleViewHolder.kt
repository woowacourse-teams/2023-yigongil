package com.created.team201.presentation.studyDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemStudyDetailStudyPeopleBinding
import com.created.team201.presentation.studyDetail.StudyMemberClickListener
import com.created.team201.presentation.studyDetail.model.StudyMemberUIModel

class StudyPeopleViewHolder(
    val binding: ItemStudyDetailStudyPeopleBinding,
    studyMemberClickListener: StudyMemberClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.studyMemberClickListener = studyMemberClickListener
    }

    fun bind(studyMemberUIModel: StudyMemberUIModel) {
        binding.studyMember = studyMemberUIModel
    }

    companion object {
        fun getBinding(parent: ViewGroup): ItemStudyDetailStudyPeopleBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemStudyDetailStudyPeopleBinding.inflate(inflater, parent, false)
        }
    }
}
