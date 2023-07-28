package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.created.team201.databinding.ItemStudyManagementOptionalTodoAddBinding
import com.created.team201.presentation.studyManagement.StudyManagementClickListener
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel

class StudyManagementOptionalTodoAddViewHolder(
    binding: ItemStudyManagementOptionalTodoAddBinding,
) : StudyOptionalTodoViewHolder(binding) {

    override fun bind(optionalTodoUiModel: OptionalTodoUiModel) {
    }

    companion object {
        fun from(
            parent: ViewGroup,
            studyManagementClickListener: StudyManagementClickListener,
        ): StudyManagementOptionalTodoAddViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemStudyManagementOptionalTodoAddBinding.inflate(inflater, parent, false)
            binding.tvItemStudyManagementOptionalTodoAddButton.setOnClickListener {
                studyManagementClickListener.onClickAddTodo(binding.etItemStudyManagementOptionalTodo.text.toString())
            }
            return StudyManagementOptionalTodoAddViewHolder(binding)
        }
    }
}
