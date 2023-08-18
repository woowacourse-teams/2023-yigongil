package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.created.team201.databinding.ItemStudyManagementOptionalTodoAddBinding
import com.created.team201.presentation.studyManagement.StudyManagementClickListener
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel

class StudyManagementOptionalTodoAddViewHolder(
    binding: ItemStudyManagementOptionalTodoAddBinding,
    studyManagementClickListener: StudyManagementClickListener,
) : StudyOptionalTodoViewHolder(binding, studyManagementClickListener) {

    init {
        binding.ivItemStudyManagementOptionalTodoAddButton.setOnClickListener {
            studyManagementClickListener.onClickAddTodo(
                false,
                binding.etItemStudyManagementOptionalTodo.text.toString(),
            )
        }
    }

    override fun bind(item: OptionalTodoUiModel) {
        (binding as ItemStudyManagementOptionalTodoAddBinding).optionalTodo = item
    }

    companion object {
        fun from(
            parent: ViewGroup,
            studyManagementClickListener: StudyManagementClickListener,
        ): StudyManagementOptionalTodoAddViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemStudyManagementOptionalTodoAddBinding.inflate(inflater, parent, false)
            return StudyManagementOptionalTodoAddViewHolder(binding, studyManagementClickListener)
        }
    }
}
