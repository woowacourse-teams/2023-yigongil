package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.created.team201.databinding.ItemStudyManagementOptionalTodoEditBinding
import com.created.team201.presentation.studyManagement.StudyManagementClickListener
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel

class StudyManagementOptionalTodoEditViewHolder private constructor(
    binding: ItemStudyManagementOptionalTodoEditBinding,
    studyManagementClickListener: StudyManagementClickListener,
    changedOptionalTodos: (OptionalTodoUiModel) -> Unit,
) : StudyOptionalTodoViewHolder(binding, studyManagementClickListener) {

    private lateinit var optionalTodo: OptionalTodoUiModel

    init {
        binding.etItemStudyManagementOptionalTodoEdit.addTextChangedListener {
            changedOptionalTodos(optionalTodo.copy(todo = optionalTodo.todo.copy(content = it.toString())))
        }
        binding.onClick = studyManagementClickListener
    }

    fun bind(item: OptionalTodoUiModel) {
        optionalTodo = item
        (binding as ItemStudyManagementOptionalTodoEditBinding).optionalTodo = optionalTodo
    }

    companion object {
        fun from(
            parent: ViewGroup,
            studyManagementClickListener: StudyManagementClickListener,
            changedOptionalTodos: (optionalTodo: OptionalTodoUiModel) -> Unit,
        ): StudyManagementOptionalTodoEditViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemStudyManagementOptionalTodoEditBinding.inflate(inflater, parent, false)
            return StudyManagementOptionalTodoEditViewHolder(
                binding,
                studyManagementClickListener,
                changedOptionalTodos,
            )
        }
    }
}
