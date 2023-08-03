package com.created.team201.presentation.studyDetail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyDetail.StudyMemberClickListener
import com.created.team201.presentation.studyDetail.model.StudyMemberUIModel

class StudyParticipantsAdapter(private val studyMemberClickListener: StudyMemberClickListener) :
    ListAdapter<StudyMemberUIModel, StudyPeopleViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyPeopleViewHolder {
        return StudyPeopleViewHolder(
            StudyPeopleViewHolder.getBinding(parent),
            studyMemberClickListener,
        )
    }

    override fun onBindViewHolder(holder: StudyPeopleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<StudyMemberUIModel>() {
            override fun areItemsTheSame(
                oldItem: StudyMemberUIModel,
                newItem: StudyMemberUIModel,
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: StudyMemberUIModel,
                newItem: StudyMemberUIModel,
            ): Boolean = oldItem == newItem
        }
    }
}
