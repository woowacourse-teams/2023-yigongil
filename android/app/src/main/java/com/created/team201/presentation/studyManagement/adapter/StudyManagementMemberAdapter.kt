package com.created.team201.presentation.studyManagement.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.presentation.studyDetail.adapter.StudyDeletedUserViewHolder
import com.created.team201.presentation.studyDetail.model.StudyPeopleViewType
import com.created.team201.presentation.studyDetail.model.StudyPeopleViewType.DELETED_MEMBER
import com.created.team201.presentation.studyDetail.model.StudyPeopleViewType.MEMBER
import com.created.team201.presentation.studyManagement.StudyMemberClickListener
import com.created.team201.presentation.studyManagement.model.StudyMemberUiModel

class StudyManagementMemberAdapter(
    private val memberClickListener: StudyMemberClickListener,
) :
    ListAdapter<StudyMemberUiModel, RecyclerView.ViewHolder>(diffCallback) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).isDeleted) {
            return DELETED_MEMBER.viewType
        }
        return MEMBER.viewType
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (StudyPeopleViewType.valueOf(viewType)) {
            MEMBER -> StudyManagementMemberViewHolder(
                StudyManagementMemberViewHolder.getBinding(parent),
                memberClickListener,
            )

            DELETED_MEMBER -> StudyDeletedUserViewHolder(
                StudyDeletedUserViewHolder.getBinding(parent),
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (StudyPeopleViewType.valueOf(getItemViewType(position))) {
            MEMBER -> (holder as StudyManagementMemberViewHolder).bind(getItem(position))
            DELETED_MEMBER -> Unit
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StudyMemberUiModel>() {
            override fun areItemsTheSame(
                oldItem: StudyMemberUiModel,
                newItem: StudyMemberUiModel,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: StudyMemberUiModel,
                newItem: StudyMemberUiModel,
            ): Boolean = oldItem == newItem
        }
    }
}
