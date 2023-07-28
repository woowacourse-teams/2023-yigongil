package com.created.team201.presentation.studyManagement.adapter

import android.view.LayoutInflater
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
            studyManagementOptionalTodoAdapter.submitList(studyManagementOptionalTodoAdapter.currentList + OptionalTodoUiModel.ADD_TODO)
        }
    }

    private fun initStudyManagementAdapter() {
        binding.rvItemStudyManagementStudyMember.adapter = studyManagementMemberAdapter
        binding.rvItemStudyManagementOptionalTodos.adapter = studyManagementOptionalTodoAdapter
    }

    // viewmodel에서 서버로부터 데이터를 가져오고 옵셔널 투두를 서브밋 리스트 해주는 과정
    // 에딧 텍스트 클릭리스너를 연결 해줘야함
    fun bind(studyManagementUIModel: StudyRoundDetailUiModel) {
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
