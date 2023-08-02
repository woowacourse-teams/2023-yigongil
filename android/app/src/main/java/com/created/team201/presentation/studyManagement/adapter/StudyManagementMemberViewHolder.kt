package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ItemStudyManagementStudyMemberBinding
import com.created.team201.presentation.studyManagement.StudyMemberClickListener
import com.created.team201.presentation.studyManagement.model.StudyMemberUiModel

class StudyManagementMemberViewHolder(
    private val binding: ItemStudyManagementStudyMemberBinding,
    memberClickListener: StudyMemberClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.onClickMember = memberClickListener
    }

    fun bind(item: StudyMemberUiModel) {
        binding.studyMember = item
        binding.tvItemStudyManagementStudyMemberEssentialTodoDone.text =
            getIsDoneDescription(item.isDone)
    }

    private fun getIsDoneDescription(isDone: Boolean): String {
        if (isDone) {
            return binding.root.context.getString(R.string.item_study_management_todo_done)
        }
        return binding.root.context.getString(R.string.item_study_management_todo_undone)
    }

    companion object {
        fun getBinding(parent: ViewGroup): ItemStudyManagementStudyMemberBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemStudyManagementStudyMemberBinding.inflate(inflater, parent, false)
        }
    }
}
