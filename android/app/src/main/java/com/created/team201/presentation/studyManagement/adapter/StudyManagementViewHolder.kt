package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemStudyManagementBinding
import com.created.team201.presentation.studyManagement.StudyManagementClickListener
import com.created.team201.presentation.studyManagement.StudyMemberClickListener
import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel
import com.created.team201.presentation.studyManagement.model.StudyRoundDetailUiModel

class StudyManagementViewHolder(
    private val binding: ItemStudyManagementBinding,
    private val studyManagementClickListener: StudyManagementClickListener,
    private val studyMemberClickListener: StudyMemberClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var studyRoundDetail: StudyRoundDetailUiModel
    private val studyManagementMemberAdapter: StudyManagementMemberAdapter by lazy {
        StudyManagementMemberAdapter(studyMemberClickListener)
    }
    private val studyManagementOptionalTodoAdapter: StudyManagementOptionalTodoAdapter by lazy {
        StudyManagementOptionalTodoAdapter(studyManagementClickListener)
    }

    init {
        initStudyManagementAdapter()
        setClickAddTodo()
    }

    private fun setClickAddTodo() {
        binding.tvStudyManagementAddOptionalTodo.setOnClickListener {
            studyManagementClickListener.onClickGenerateOptionalTodo(studyManagementOptionalTodoAdapter.itemCount)
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
            studyManagementClickListener.clickOnUpdateTodo(
                true,
                binding.etItemStudyManagementEssentialTodoContent.text.toString(),
            )
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

    companion object {
        fun getBinding(parent: ViewGroup): ItemStudyManagementBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemStudyManagementBinding.inflate(inflater, parent, false)
        }
    }
}
