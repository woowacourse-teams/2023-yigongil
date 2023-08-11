package com.created.team201.presentation.studyManagement.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyManagement.StudyManagementClickListener
import com.created.team201.presentation.studyManagement.adapter.OptionalTodoViewType.ADD
import com.created.team201.presentation.studyManagement.adapter.OptionalTodoViewType.DISPLAY
import com.created.team201.presentation.studyManagement.adapter.OptionalTodoViewType.EDIT
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel

class StudyManagementOptionalTodoAdapter(
    private val studyManagementClickListener: StudyManagementClickListener,
    private val changedOptionalTodos: (optionalTodo: OptionalTodoUiModel) -> Unit,
) :
    ListAdapter<OptionalTodoUiModel, StudyOptionalTodoViewHolder>(
        diffCallback,
    ) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].todo.todoId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyOptionalTodoViewHolder {
        return when (OptionalTodoViewType.valueOf(viewType)) {
            ADD -> StudyManagementOptionalTodoAddViewHolder.from(parent, studyManagementClickListener)
            DISPLAY -> StudyManagementOptionalTodoViewHolder.from(parent, studyManagementClickListener)
            EDIT -> StudyManagementOptionalTodoEditViewHolder.from(parent, studyManagementClickListener, changedOptionalTodos)
        }
    }

    override fun onBindViewHolder(holder: StudyOptionalTodoViewHolder, position: Int) {
        when (OptionalTodoViewType.valueOf(getItemViewType(position))) {
            ADD -> (holder as StudyManagementOptionalTodoAddViewHolder).bind(getItem(position))
            DISPLAY -> (holder as StudyManagementOptionalTodoViewHolder).bind(getItem(position))
            EDIT -> (holder as StudyManagementOptionalTodoEditViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return (getItem(position).viewType)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<OptionalTodoUiModel>() {
            override fun areItemsTheSame(
                oldItem: OptionalTodoUiModel,
                newItem: OptionalTodoUiModel,
            ): Boolean = oldItem.todo.todoId == newItem.todo.todoId

            override fun areContentsTheSame(
                oldItem: OptionalTodoUiModel,
                newItem: OptionalTodoUiModel,
            ): Boolean = oldItem == newItem
        }
    }
}
