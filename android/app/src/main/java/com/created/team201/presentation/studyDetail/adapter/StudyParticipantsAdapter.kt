package com.created.team201.presentation.studyDetail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyDetail.model.StudyMember

class StudyParticipantsAdapter :
    ListAdapter<StudyMember, StudyPeopleViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyPeopleViewHolder {
        return StudyPeopleViewHolder(StudyPeopleViewHolder.getBinding(parent))
    }

    override fun onBindViewHolder(holder: StudyPeopleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<StudyMember>() {
            override fun areItemsTheSame(
                oldItem: StudyMember,
                newItem: StudyMember,
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: StudyMember,
                newItem: StudyMember,
            ): Boolean = oldItem == newItem
        }
    }
}
