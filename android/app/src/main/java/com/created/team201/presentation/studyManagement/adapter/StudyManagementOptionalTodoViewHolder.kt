package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.created.team201.databinding.ItemStudyManagementOptionalTodosBinding
import com.created.team201.presentation.studyManagement.StudyManagementClickListener
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel

class StudyManagementOptionalTodoViewHolder private constructor(
    binding: ItemStudyManagementOptionalTodosBinding,
) : StudyOptionalTodoViewHolder(binding) {

    override fun bind(optionalTodoUiModel: OptionalTodoUiModel) {
        (binding as ItemStudyManagementOptionalTodosBinding).optionalTodo = optionalTodoUiModel
    }

    companion object {
        fun from(
            parent: ViewGroup,
            studyManagementClickListener: StudyManagementClickListener,
        ): StudyManagementOptionalTodoViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemStudyManagementOptionalTodosBinding.inflate(inflater, parent, false)
            binding.onClick = studyManagementClickListener
            return StudyManagementOptionalTodoViewHolder(binding)
        }
    }
}
