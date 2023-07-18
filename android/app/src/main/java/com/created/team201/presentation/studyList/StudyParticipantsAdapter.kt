package com.created.team201.presentation.studyList

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyList.uiModel.StudyParticipant

class StudyParticipantsAdapter :
    ListAdapter<StudyParticipant, StudyPeopleViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyPeopleViewHolder {
        return StudyPeopleViewHolder(StudyPeopleViewHolder.getBinding(parent))
    }

    override fun onBindViewHolder(holder: StudyPeopleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<StudyParticipant>() {
            override fun areItemsTheSame(
                oldItem: StudyParticipant,
                newItem: StudyParticipant,
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: StudyParticipant,
                newItem: StudyParticipant,
            ): Boolean = oldItem == newItem
        }
    }
}
