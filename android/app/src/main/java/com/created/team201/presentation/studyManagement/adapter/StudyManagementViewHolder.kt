package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ItemStudyManagementBinding
import com.created.team201.presentation.studyManagement.StudyManagementClickListener
import com.created.team201.presentation.studyManagement.StudyMemberClickListener
import com.created.team201.presentation.studyManagement.TodoState.DEFAULT
import com.created.team201.presentation.studyManagement.TodoState.NECESSARY_TODO_EDIT
import com.created.team201.presentation.studyManagement.TodoState.OPTIONAL_TODO_ADD
import com.created.team201.presentation.studyManagement.TodoState.OPTIONAL_TODO_EDIT
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel

class StudyManagementViewHolder(
    private val binding: ItemStudyManagementBinding,
    private val studyManagementClickListener: StudyManagementClickListener,
    private val studyMemberClickListener: StudyMemberClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var studyRoundDetail: StudyRoundDetailUiModel
    private var updatedOptionalTodoUiModels: MutableList<OptionalTodoUiModel> = mutableListOf()
    private val studyManagementMemberAdapter: StudyManagementMemberAdapter by lazy {
        StudyManagementMemberAdapter(studyMemberClickListener)
    }
    private val studyManagementOptionalTodoAdapter: StudyManagementOptionalTodoAdapter by lazy {
        StudyManagementOptionalTodoAdapter(studyManagementClickListener, ::changedOptionalTodos)
    }

    init {
        initStudyManagementAdapter()
        setClickAddTodo()
    }

    private fun setClickAddTodo() {
        binding.tvStudyManagementAddOptionalTodo.setOnClickListener {
            val todoState = studyManagementClickListener.onClickGenerateOptionalTodo(
                studyManagementOptionalTodoAdapter.itemCount,
            )
            if (todoState != OPTIONAL_TODO_ADD) {
                return@setOnClickListener
            }
            val currentTodos = studyManagementOptionalTodoAdapter.currentList

            if (currentTodos.isEmpty()) {
                binding.rvItemStudyManagementOptionalTodos.visibility = VISIBLE
                studyManagementOptionalTodoAdapter.submitList(listOf(OptionalTodoUiModel.ADD_TODO.copy()))
                return@setOnClickListener
            }
            if (currentTodos.last().viewType == OptionalTodoViewType.ADD.viewType || studyManagementOptionalTodoAdapter.itemCount >= 4) {
                return@setOnClickListener
            }

            studyManagementOptionalTodoAdapter.submitList(currentTodos + OptionalTodoUiModel.ADD_TODO.copy())
        }

        binding.tvItemStudyManagementEdit.setOnClickListener {
            setEditNecessaryTodo()
        }

        binding.tvItemStudyManagementEditOptinalTodo.setOnClickListener {
            setEditOptionalTodos()
        }

        binding.ivStudyManagementNecessaryTodoAddButton.setOnClickListener {
            studyManagementClickListener.onClickAddTodo(
                true,
                binding.etItemStudyManagementEssentialTodoContent.text.toString(),
            )
        }
    }

    private fun initStudyManagementAdapter() {
        binding.onClick = studyManagementClickListener
        binding.rvItemStudyManagementStudyMember.adapter = studyManagementMemberAdapter
        binding.rvItemStudyManagementOptionalTodos.apply {
            adapter = studyManagementOptionalTodoAdapter
            itemAnimator = null
        }
    }

    fun bind(studyManagementUIModel: StudyRoundDetailUiModel) {
        studyRoundDetail = studyManagementUIModel
        binding.studyManagement = studyManagementUIModel
        studyManagementMemberAdapter.submitList(studyManagementUIModel.studyMembers)
        studyManagementOptionalTodoAdapter.submitList(studyManagementUIModel.optionalTodos)
    }

    private fun setEditNecessaryTodo() {
        val currentContent = binding.etItemStudyManagementEssentialTodoContent.text.toString()
        when (studyManagementClickListener.onClickEditNecessaryTodo(currentContent)) {
            DEFAULT -> {
                binding.etItemStudyManagementEssentialTodoContent.isEnabled = false
                binding.tvItemStudyManagementEdit.setText(R.string.study_management_edit_todo)
            }

            NECESSARY_TODO_EDIT -> {
                binding.etItemStudyManagementEssentialTodoContent.isEnabled = true
                binding.tvItemStudyManagementEdit.setText(R.string.study_management_edit_todo_done)
            }

            else -> return
        }
    }

    private fun setEditOptionalTodos() {
        when (studyManagementClickListener.onClickEditOptionalTodo(updatedOptionalTodoUiModels.toList())) {
            DEFAULT -> {
                val updatedOptionalTodos = studyManagementOptionalTodoAdapter.currentList.map {
                    it.copy(viewType = OptionalTodoViewType.DISPLAY.viewType)
                }
                studyManagementOptionalTodoAdapter.submitList(updatedOptionalTodos)
                binding.tvItemStudyManagementEditOptinalTodo.setText(R.string.study_management_edit_todo)
            }

            OPTIONAL_TODO_EDIT -> {
                val updatedOptionalTodos = studyManagementOptionalTodoAdapter.currentList.map {
                    it.copy(viewType = OptionalTodoViewType.EDIT.viewType)
                }
                updatedOptionalTodoUiModels = mutableListOf()
                studyManagementOptionalTodoAdapter.submitList(updatedOptionalTodos)
                binding.tvItemStudyManagementEditOptinalTodo.setText(R.string.study_management_edit_todo_done)
            }

            else -> return
        }
    }

    private fun changedOptionalTodos(updatedTodo: OptionalTodoUiModel) {
        updatedOptionalTodoUiModels.removeIf {
            it.todo.todoId == updatedTodo.todo.todoId
        }
        updatedOptionalTodoUiModels.add(updatedTodo)
    }

    companion object {
        fun getBinding(parent: ViewGroup): ItemStudyManagementBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemStudyManagementBinding.inflate(inflater, parent, false)
        }
    }
}
