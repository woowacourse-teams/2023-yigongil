package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.created.team201.databinding.ItemStudyManagementOptionalTodosBinding
import com.created.team201.presentation.studyManagement.StudyManagementClickListener
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel

class StudyManagementOptionalTodoViewHolder private constructor(
    binding: ItemStudyManagementOptionalTodosBinding,
    studyManagementClickListener: StudyManagementClickListener,
) : StudyOptionalTodoViewHolder(binding, studyManagementClickListener) {

    init {
        binding.onClick = studyManagementClickListener
    }

    override fun bind(item: OptionalTodoUiModel) {
        (binding as ItemStudyManagementOptionalTodosBinding).optionalTodo = item
    }

    companion object {
        fun from(
            parent: ViewGroup,
            studyManagementClickListener: StudyManagementClickListener,
        ): StudyManagementOptionalTodoViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemStudyManagementOptionalTodosBinding.inflate(inflater, parent, false)
            return StudyManagementOptionalTodoViewHolder(binding, studyManagementClickListener)
        }
    }
}
