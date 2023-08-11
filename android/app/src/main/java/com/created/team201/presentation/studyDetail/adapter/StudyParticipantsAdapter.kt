package com.created.team201.presentation.studyDetail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.created.team201.presentation.studyDetail.StudyMemberClickListener
import com.created.team201.presentation.studyDetail.model.StudyMemberUIModel
import com.created.team201.presentation.studyDetail.model.StudyPeopleViewType
import com.created.team201.presentation.studyDetail.model.StudyPeopleViewType.DELETED_MEMBER
import com.created.team201.presentation.studyDetail.model.StudyPeopleViewType.MEMBER

class StudyParticipantsAdapter(private val studyMemberClickListener: StudyMemberClickListener) :
    ListAdapter<StudyMemberUIModel, ViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (StudyPeopleViewType.valueOf(viewType)) {
            MEMBER -> StudyPeopleViewHolder(
                StudyPeopleViewHolder.getBinding(parent),
                studyMemberClickListener,
            )

            DELETED_MEMBER -> StudyDeletedUserViewHolder(
                StudyDeletedUserViewHolder.getBinding(parent),
            )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (StudyPeopleViewType.valueOf(getItemViewType(position))) {
            MEMBER -> (holder as StudyPeopleViewHolder).bind(getItem(position))
            DELETED_MEMBER -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).isDeleted) {
            return DELETED_MEMBER.viewType
        }
        return MEMBER.viewType
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
